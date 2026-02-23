package com.xebisco.yield2d.engine.openglimpl;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class GrowableMat4fStack extends Matrix4f {
    private static final long serialVersionUID = 1L;
    private Matrix4f[] mats;
    private int curr;

    public GrowableMat4fStack(int stackSize) {
        if (stackSize < 1) {
            throw new IllegalArgumentException("stackSize must be >= 1");
        } else {
            this.mats = new Matrix4f[stackSize - 1];

            for (int i = 0; i < this.mats.length; ++i) {
                this.mats[i] = new Matrix4f();
            }

        }
    }

    public GrowableMat4fStack() {
    }

    public GrowableMat4fStack clear() {
        this.curr = 0;
        this.identity();
        return this;
    }

    public GrowableMat4fStack pushMatrix() {
        if (this.curr == this.mats.length) {
            Matrix4f[] oldMats = this.mats;
            this.mats = new Matrix4f[this.mats.length + 1];
            System.arraycopy(oldMats, 0, this.mats, 0, oldMats.length);
            mats[mats.length - 1] = new Matrix4f();
        }
        this.mats[this.curr++].set(this);
        return this;
    }

    public GrowableMat4fStack popMatrix() {
        if (this.curr == 0) {
            throw new IllegalStateException("already at the bottom of the stack");
        } else {
            this.set(this.mats[--this.curr]);
            return this;
        }
    }

    public int hashCode() {
        int prime = 31;
        int result = super.hashCode();
        result = 31 * result + this.curr;

        for (int i = 0; i < this.curr; ++i) {
            result = 31 * result + this.mats[i].hashCode();
        }

        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else {
            if (obj instanceof GrowableMat4fStack) {
                GrowableMat4fStack other = (GrowableMat4fStack) obj;
                if (this.curr != other.curr) {
                    return false;
                }

                for (int i = 0; i < this.curr; ++i) {
                    if (!this.mats[i].equals(other.mats[i])) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(this.curr);

        for (int i = 0; i < this.curr; ++i) {
            out.writeObject(this.mats[i]);
        }

    }

    public void readExternal(ObjectInput in) throws IOException {
        super.readExternal(in);
        this.curr = in.readInt();
        this.mats = new Matrix4fStack[this.curr];

        for (int i = 0; i < this.curr; ++i) {
            Matrix4f m = new Matrix4f();
            m.readExternal(in);
            this.mats[i] = m;
        }

    }

    public Object clone() throws CloneNotSupportedException {
        GrowableMat4fStack cloned = (GrowableMat4fStack) super.clone();
        Matrix4f[] clonedMats = new Matrix4f[this.mats.length];

        for (int i = 0; i < this.mats.length; ++i) {
            clonedMats[i] = (Matrix4f) this.mats[i].clone();
        }

        cloned.mats = clonedMats;
        return cloned;
    }
}
