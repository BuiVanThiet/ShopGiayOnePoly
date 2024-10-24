package com.example.shopgiayonepoly.entites;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CanvasjsChartData {
    static Map<Object,Object> map = null;
    static List<List<Map<Object,Object>>> list = new ArrayList<List<Map<Object,Object>>>();
    static List<Map<Object,Object>> dataPoints1 = new ArrayList<Map<Object,Object>>();
    static List<Map<Object,Object>> dataPoints2 = new ArrayList<Map<Object,Object>>();

    static {
        map = new HashMap<Object,Object>(); map.put("label", "Jan 1"); map.put("y", 2.2984482);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 2"); map.put("y", 1.6430263);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 3"); map.put("y", 1.7507812);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 4"); map.put("y", 1.5731722);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 5"); map.put("y", 1.177317);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 6"); map.put("y", 1.2797851);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 7"); map.put("y", 1.4864893);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 8"); map.put("y", 1.7770796);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 9"); map.put("y", 1.4806903);dataPoints1.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 10"); map.put("y", 1.537653);dataPoints1.add(map);

        map = new HashMap<Object,Object>(); map.put("label", "Jan 1"); map.put("y", 15);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 2"); map.put("y", 57);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 3"); map.put("y", 96);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 4"); map.put("y", 119);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 5"); map.put("y", 144);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 6"); map.put("y", 128);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 7"); map.put("y", 47);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 8"); map.put("y", 57);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 9"); map.put("y", 120);dataPoints2.add(map);
        map = new HashMap<Object,Object>(); map.put("label", "Jan 10"); map.put("y", 123);dataPoints2.add(map);

        list.add(dataPoints1);
        list.add(dataPoints2);
    }

    public static List<List<Map<Object, Object>>> getCanvasjsDataList() {
        return list;
    }
}
