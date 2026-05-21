package dev.nittwit.intercraft.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NameManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();
    private final RandomSource random = RandomSource.create();

    private List<String> names = List.of();

    public NameManager() {
        super(GSON, "names");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data,
                         ResourceManager resourceManager,
                         ProfilerFiller profiler) {

        List<String> loaded = new ArrayList<>();

        for (JsonElement element : data.values()) {
            if (element.isJsonArray()) {
                for (JsonElement entry : element.getAsJsonArray()) {
                    loaded.add(entry.getAsString());
                }
            }
        }

        this.names = List.copyOf(loaded);
    }

    public String getRandomName() {
        if (names.isEmpty()) return "Villager";
        return names.get(random.nextInt(names.size()));
    }
}