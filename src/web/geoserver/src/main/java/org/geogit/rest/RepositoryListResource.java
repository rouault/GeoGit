package org.geogit.rest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.rest.MapResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.FreemarkerFormat;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RepositoryListResource extends MapResource {

    public RepositoryListResource() {
        super();
    }

    @Override
    protected List<DataFormat> createSupportedFormats(Request request, Response response) {
        List<DataFormat> formats = Lists.newArrayListWithCapacity(3);

        formats.add(new FreemarkerFormat("templates/index.ftl", getClass(), MediaType.TEXT_HTML));

        return formats;
    }

    @Override
    public Map<String, Object> getMap() throws Exception {
        List<String> repoNames = getRepoNames();

        Map<String, Object> map = Maps.newHashMap();
        map.put("repositories", repoNames);
        map.put("page", getPageInfo());
        return map;
    }

    private List<String> getRepoNames() {
        Request request = getRequest();
        Map<String, Object> attributes = request.getAttributes();
        @SuppressWarnings("unchecked")
        List<DataStoreInfo> geogitStores = (List<DataStoreInfo>) attributes.get("stores");

        List<String> repoNames = Lists.newArrayListWithCapacity(geogitStores.size());
        for (DataStoreInfo info : geogitStores) {
            String wsname = info.getWorkspace().getName();
            String storename = info.getName();
            String repoName = wsname + ":" + storename + ".geogit";
            repoNames.add(repoName);
        }
        Collections.sort(repoNames);
        return repoNames;
    }
}