package ru.pylaev.dreamscreentime;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CacheConfig(cacheNames = "Users")
public class UserContextRepository {
    private final Map<Long, List<Integer>> cats = new ConcurrentHashMap<>();
    private final Map<Long, List<Integer>> dreamTimes = new ConcurrentHashMap<>();

    @Cacheable(key = "#a0.toString().concat('-cat')")
    public List<Integer> getUsedCatImageIndexesByUser(Long userId) {
        return cats.getOrDefault(userId, List.of());
    }

    @CachePut(key = "#a0.toString().concat('-cat')")
    public List<Integer> updateUsedCatImageIndexesByUser(Long userId, List<Integer> indexes) {
        cats.put(userId, indexes);
        return indexes;
    }

    @Cacheable(key = "#a0.toString().concat('-dreamTime')")
    public List<Integer> getUsedDreamTimeImageIndexesByUser(Long userId) {
        return dreamTimes.getOrDefault(userId, List.of());
    }

    @CachePut(key = "#a0.toString().concat('-dreamTime')")
    public List<Integer> updateUsedDreamTimeImageIndexesByUser(Long userId, List<Integer> indexes) {
        dreamTimes.put(userId, indexes);
        return indexes;
    }
}
