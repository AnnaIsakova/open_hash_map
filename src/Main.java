
public class Main {
    public static void main(String[] args) {
        OpenHashMap map = new OpenHashMap();
        System.out.println(map); //{}
        map.put(1, 111);
        map.put(2, 222);
        map.put(12, 1212);
        map.put(3, 333);
        System.out.println(map); //{1 = 111, 2 = 222, 12 = 1212, 3 = 333}
        map.put(19, 1919);
        System.out.println(map);//{1 = 111, 2 = 222, 3 = 333, 19 = 1919, 12 = 1212}
        System.out.println(map.get(3));//333
        System.out.println(map.get(8));//null
    }
}
