package net.myplayplanet.bwinfbackend.service.mock;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RandomizerService {

    public <T> T getRandomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }

        // If it's a List, we can access by index directly
        if (collection instanceof List<T> list) {
            int randomIndex = ThreadLocalRandom.current().nextInt(list.size());
            return list.get(randomIndex);
        }

        // For generic Collection, convert to List
        List<T> list = new ArrayList<>(collection);
        int randomIndex = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(randomIndex);
    }

}
