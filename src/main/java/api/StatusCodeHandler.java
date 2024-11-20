package api;

import io.cucumber.java.sl.In;

import java.util.*;

public class StatusCodeHandler {

    private final Map<Method, List<Integer>> codeMap;
    private List<Integer> overrideList = null;


    public StatusCodeHandler(Map<Method, List<Integer>> codeMap) {
        this.codeMap = codeMap;
    }

    public StatusCodeHandler () {
        this.codeMap = new HashMap<>();
        this.codeMap.put(Method.GET, Arrays.asList(200, 403));
        this.codeMap.put(Method.POST, Arrays.asList(200, 201, 202, 204));
        this.codeMap.put(Method.PUT, Arrays.asList(200, 204));
        this.codeMap.put(Method.DELETE, Collections.singletonList(204));
    }

    public void put (Method method, List<Integer> codes) {
        this.codeMap.put(method, codes);
    }

    public List<Integer> get (Method method) {
        return (List)this.codeMap.get(method);
    }

    public Set<Method> keySet (Method method, List<Integer> codes) {
        return this.codeMap.keySet();
    }

    public Map<Method, List<Integer>> getCodeMap () {
        return this.codeMap;
    }

    public void setOverrideList (List<Integer> codes) {
        this.overrideList = codes;
    }

    public List<Integer> getAndRemoveOverrideList () {
        List<Integer> result = new ArrayList<>(this.overrideList);
        this.overrideList = null;
        return result;
    }

    public boolean isOverrideListSet () {
        if (this.overrideList == null) {
            return false;
        } else {
            return !this.overrideList.isEmpty();
        }
    }
}
