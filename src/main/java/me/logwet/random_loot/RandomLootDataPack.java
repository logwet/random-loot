package me.logwet.random_loot;

import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RandomLootDataPack extends DefaultResourcePack {
    private Map<Identifier, Identifier> shuffledIdentifierMapping;
    private boolean hasShuffled = false;

    public RandomLootDataPack(String[] namespaces) {
        super(namespaces);
    }

    private void shuffle() {
        RandomLoot.log(Level.INFO, "Shuffling loot tables...");

        RandomLoot.newRandomInstance();

        List<Identifier> identifiers = new ArrayList<>(this.findResources(ResourceType.SERVER_DATA, namespaces.iterator().next(), "loot_tables", Integer.MAX_VALUE, (string) -> string.endsWith(".json")));
        Iterator<Identifier> identifierIterator = identifiers.iterator();

        List<Identifier> shuffledIdentifiers = new ArrayList<>(identifiers);
        Collections.shuffle(shuffledIdentifiers, RandomLoot.getRandomInstance());

        shuffledIdentifierMapping = shuffledIdentifiers
                .stream()
                .collect(Collectors.toMap(i -> identifierIterator.next(), Function.identity()));

        hasShuffled = true;
    }


    public InputStream open(ResourceType type, Identifier id) throws IOException {
        if (type == ResourceType.SERVER_DATA) {
            if (id.getPath().startsWith("loot_tables")) {
                if (!hasShuffled) {
                    shuffle();
                }

                id = shuffledIdentifierMapping.get(id);
            }
        }

        return super.open(type, id);
    }

    public String getName() {
        return "RandomLoot";
    }
}
