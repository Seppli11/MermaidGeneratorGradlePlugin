package ch.zhaw.catan;
public class Tuple<X, Y> {
    public final X first;

    public final Y second;

    public Tuple(X x, Y y) {
        this.first = x;
        this.second = y;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("(" + first) + ",") + second) + ")";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ch.zhaw.catan.Tuple)) {
            return false;
        }
        @java.lang.SuppressWarnings("unchecked")
        ch.zhaw.catan.Tuple<X, Y> otherCasted = ((ch.zhaw.catan.Tuple<X, Y>) (other));
        // null is not a valid value for first and second tuple element
        return otherCasted.first.equals(this.first) && otherCasted.second.equals(this.second);
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (first == null ? 0 : first.hashCode());
        result = (prime * result) + (second == null ? 0 : second.hashCode());
        return result;
    }
}