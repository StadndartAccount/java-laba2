import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage {
    private HashMap<String, Object> _data;

    public DataStorage() {
        _data = new HashMap<String, Object>();
    }

    public Integer GetInteger(String key) {
        return (Integer) _data.get(key);
    }

    public Float GetFloat(String key) {
        return (Float) _data.get(key);
    }

    public String GetString(String key) {
        return (String) _data.get(key);
    }

    public HashMap<String, Integer> GetMapStringInteger(String key) {
        return (HashMap<String, Integer>) _data.get(key);
    }

    public void AddValue(String key, Object value) {
        _data.put(key, value);
    }

    public Integer GetSize() {
        return _data.size();
    }
}
