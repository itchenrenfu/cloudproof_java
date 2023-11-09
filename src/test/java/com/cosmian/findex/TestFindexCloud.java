//package com.cosmian.findex;

//import static org.junit.jupiter.api.Assertions.assertEquals;

//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Optional;

//import org.junit.jupiter.api.Test;

//import com.cosmian.TestUtils;
//import com.cosmian.jna.findex.FindexCloud;
//import com.cosmian.jna.findex.ffi.SearchResults;
//import com.cosmian.jna.findex.ffi.UpsertResults;
//import com.cosmian.jna.findex.structs.Location;
//import com.cosmian.utils.RestClient;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;

//public class TestFindexCloud {
    //@Test
    //public void testFindexCloud() throws Exception {
        //ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //if (!TestUtils.serverAvailable(FindexCloud.findexCloudUrl())) {
            //throw new RuntimeException("Findex cloud is down");
        //}

        //String label = "Hello World!";

        //RestClient client = new RestClient(FindexCloud.findexCloudUrl(), Optional.empty());
        //String response = client.json_post("/indexes", "{ \"name\": \"Test\" }");

        //Index index = mapper.readValue(response, Index.class);

        //String token = FindexCloud.generateNewToken(index.publicId, index.fetchEntriesKey, index.fetchChainsKey,
            //index.upsertEntriesKey, index.insertChainsKey);

        //System.out.println("");
        //System.out.println("---------------------------------------");
        //System.out.println("Findex Rest upsert");
        //System.out.println("---------------------------------------");
        //System.out.println("");

        //FindexCloud.IndexRequest indexRequest = new FindexCloud.IndexRequest(token, label)
            //.add(new Location(1337), new String[] {"John", "Doe"})
            //.add(new Location(42), new String[] {"Jane", "Doe"});
        //UpsertResults res = FindexCloud.upsert(indexRequest);
        //assertEquals(3, res.getResults().size(), "wrong number of new keywords returned");

        //System.out.println("");
        //System.out.println("---------------------------------------");
        //System.out.println("Findex Rest search");
        //System.out.println("---------------------------------------");
        //System.out.println("");

        //FindexCloud.SearchRequest searchRequest = new FindexCloud.SearchRequest(token, label)
            //.keywords(new String[] {"Doe"});
        //SearchResults searchResults = FindexCloud.search(searchRequest);
        //assertEquals(new HashSet<>(Arrays.asList(new Long(1337), new Long(42))), searchResults.getNumbers());
    //}

    //public static class Index {
        //@JsonProperty(value = "public_id")
        //String publicId;

        //@JsonProperty(value = "fetch_entries_key")
        //byte[] fetchEntriesKey;

        //@JsonProperty(value = "fetch_chains_key")
        //byte[] fetchChainsKey;

        //@JsonProperty(value = "upsert_entries_key")
        //byte[] upsertEntriesKey;

        //@JsonProperty(value = "insert_chains_key")
        //byte[] insertChainsKey;

        //Index() {
        //}
    //}
//}
