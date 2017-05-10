import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.index.query.QueryBuilders.*;


public class ElasticSearchApp {
    public static void main(String[] args){
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();
        TransportClient client = null;
        try {
            client =  new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        String json = "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//        IndexResponse response = client.prepareIndex("twitter", "tweet")
//                .setSource(json)
//                .get();

        QueryBuilder qb = nestedQuery("categories",
                boolQuery()
                        .must(matchQuery("categories.L0", "A"))
                        .must(matchQuery("categories.L1", "B")), ScoreMode.Avg );

        SearchResponse searchResponse =  client.prepareSearch("customer_nested")
                .setTypes("my_type")
                .setQuery(qb).execute().actionGet();
        System.out.print(searchResponse);
    }


}
