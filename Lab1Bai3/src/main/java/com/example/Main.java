package com.example;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    // Kiểm tra tính chất của 3 điểm (p, q, r)
    // bằng công thức tích có hướng của hai vectors pq và qr
    // 0: 3 điểm thẳng hàng
    // < 0: 3 điểm tạo góc quay theo chiều kim đồng hồ
    // > 0: 3 điểm tạo góc quay ngược chiều kim đồng hồ
    public static int orientation(Station p, Station q, Station r)
    {
        return (q.x - p.x) * (r.y - q.y) - (q.y - p.y) * (r.x - q.x);
    }

    public static List<Station> selectAlertStations(List<Station> stations, int n) {
        if (n < 3) return stations;

        int k = 0;
        List<Station> ans = new ArrayList<>(2 * n);

        Collections.sort(stations);

        // Giải thuật Convex Hull | Monotone chain algorithm
        // https://www.geeksforgeeks.org/convex-hull-monotone-chain-algorithm/
        for(int i = 0; i < n; ++i) {
            while(k >= 2 && orientation(ans.get(k - 2), ans.get(k - 1), stations.get(i)) <= 0)
                ans.remove(--k);
            ans.add(stations.get(i));
            k++;
        }

        for(int i = n - 2, t = k; i >= 0; --i) {
            while(k > t && orientation(ans.get(k - 2), ans.get(k - 1), stations.get(i)) <= 0)
                ans.remove(--k);
            ans.add(stations.get(i));
            k++;
        }

        ans.remove(ans.size() - 1);

        return ans;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập số lượng trạm: ");
        int n = scanner.nextInt();
        List<Station> stations = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.print("Nhập tọa độ x y cho trạm " + (i + 1) + ": ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            stations.add(new Station(x, y));
        }

        List<Station> alertStations = selectAlertStations(stations, n);

        System.out.println("Các trạm được chọn làm trạm cảnh báo: ");
        for (Station s : alertStations) {
            System.out.println(s);
        }

        scanner.close();
    }
}