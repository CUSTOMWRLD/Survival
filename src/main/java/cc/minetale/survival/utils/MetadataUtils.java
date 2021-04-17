package cc.minetale.survival.utils;

import com.customwrld.customlib.util.CollectionsUtil;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MetadataUtils {

    /***
     * Gets the list under the given metadata key, or returns the default list.
     * @param metadatable The metadatable to get the list from
     * @param key The metadata key
     * @param defaultList The default list
     * @param type Class representing the type
     * @param <T> Type of values inside the list
     * @return List retrieved from metadata or provided list
     */
    public static <T> List<T> getListOrDefault(Metadatable metadatable, String key, List<T> defaultList, Class<T> type) {

        List<T> list;

        if(metadatable.hasMetadata(key)) {

            Object obj = metadatable.getMetadata(key).get(0).value();

            if (obj instanceof List) {
                list = CollectionsUtil.genericListToType((List<?>) obj, type);
            } else {
                list = defaultList;
            }

        } else {

            list = defaultList;

        }

        return list;

    }

    /***
     * Gets the list under the given metadata key, or returns an empty {@link ArrayList}.
     * @param metadatable The metadatable to get the list from
     * @param key The metadata key
     * @param type Class representing the type
     * @param <T> Type of values inside the list
     * @return List retrieved from metadata or empty {@link ArrayList}
     */
    public static <T> List<T> getListOrEmpty(Metadatable metadatable, String key, Class<T> type) {
        return getListOrDefault(metadatable, key, new ArrayList<>(), type);
    }

    /***
     * Gets the list under the given metadata key, or returns the default list and sets it as metadata.
     * @param plugin Plugin for setting the metadata
     * @param metadatable The metadatable to get the list from
     * @param key The metadata key
     * @param defaultList The default list
     * @param type Class representing the type
     * @param <T> Type of values inside the list
     * @return List retrieved from metadata or provided list
     */
    public static <T> List<T> getListOrDefaultAndSet(JavaPlugin plugin, Metadatable metadatable, String key, List<T> defaultList, Class<T> type) {

        List<T> list;

        if(metadatable.hasMetadata(key)) {

            Object obj = metadatable.getMetadata(key).get(0).value();

            if (obj instanceof List) {
                list = CollectionsUtil.genericListToType((List<?>) obj, type);
            } else {
                list = defaultList;
            }

        } else {

            list = defaultList;
            metadatable.setMetadata(key, new FixedMetadataValue(plugin, defaultList));

        }

        return list;

    }

    /***
     * Gets the list under the given metadata key, or returns an empty {@link ArrayList} and sets it as metadata.
     * @param plugin Plugin for setting the metadata
     * @param metadatable The metadatable to get the list from
     * @param key The metadata key
     * @param type Class representing the type
     * @param <T> Type of values inside the list
     * @return List retrieved from metadata or empty {@link ArrayList}
     */
    public static <T> List<T> getListOrEmptyAndSet(JavaPlugin plugin, Metadatable metadatable, String key, Class<T> type) {
        return getListOrDefaultAndSet(plugin, metadatable, key, new ArrayList<>(), type);
    }

    /***
     * Removes the element from a list under the given metadata key.
     * @param metadatable The metadatable to get the list from
     * @param key The metadata key
     * @param type Class representing the type
     * @param <T> Type of values inside the list
     * @return List retrieved from metadata or empty {@link ArrayList}
     */
    public static <T> List<T> removeElement(Metadatable metadatable, String key, T element, Class<T> type) {
        List<T> list = getListOrEmpty(metadatable, key, type);

        list.remove(element);

        return list;
    }

    /***
     * Removes the element from a list under the given metadata key, or deletes the metadata if that list is empty.
     * @param plugin Plugin for removing the metadata
     * @param metadatable The metadatable to get the list from
     * @param key The metadata key
     * @param type Class representing the type
     * @param <T> Type of values inside the list
     * @return List retrieved from metadata or empty {@link ArrayList}
     */
    public static <T> List<T> removeElementAndDeleteIfEmpty(JavaPlugin plugin, Metadatable metadatable, String key, T element, Class<T> type) {
        List<T> list = removeElement(metadatable, key, element, type);

        if(list.isEmpty()) {
            metadatable.removeMetadata(key, plugin);
        }

        return list;
    }

}
