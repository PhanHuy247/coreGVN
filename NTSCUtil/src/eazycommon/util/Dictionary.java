/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.util;

import java.util.HashMap;
import java.util.Map;

public class Dictionary<K1, K2, V> {

    private final Map<String, V> map;

    public Dictionary() {
        map = new HashMap<>();
    }

    public void put(K1 key1, K2 key2, V value) {
        map.put(createKey(key1, key2), value);
    }

    public V get(K1 key1, K2 key2) {
        return map.get(createKey(key1, key2));
    }

    public boolean containsKeys(K1 key1, K2 key2) {
        return map.containsKey(createKey(key1, key2));
    }

    private String createKey(K1 key1, K2 key2) {
        StringBuilder builder = new StringBuilder();

        builder.append(System.identityHashCode(key1));
        builder.append("_");
        builder.append(System.identityHashCode(key2));

        return builder.toString();
    }
    
    public int getSize(){
        return map.size();
    }
}
