package com.example;

public class Station implements Comparable<Station> {
    int x, y;

    public Station(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Station p)
    {
        return Integer.compare(x, p.x) != 0
                ? Integer.compare(x, p.x)
                : Integer.compare(y, p.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
