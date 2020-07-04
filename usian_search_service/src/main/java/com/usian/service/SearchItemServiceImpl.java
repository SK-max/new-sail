package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.usian.JsonUtils;
import com.usian.mapper.SearchItemMapper;
import com.usian.pojo.SearchItem;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Value("${ES_INDEX_NAME}")
    private String ES_INDEX_NAME;

    @Value("${ES_TYPE_NAME}")
    private String ES_TYPE_NAME;


    @Override
    public Boolean importAll() {
        try {
            if (!isExistsIndex()) {
                boolean index = createIndex();
            }

            int page = 1;
            while (true) {
                PageHelper.startPage(page, 1000);
                List<SearchItem> list = searchItemMapper.getItemList();
                if (list == null || list.size() == 0) {
                    break;
                }
                BulkRequest bulkRequest = new BulkRequest();
                for (int i = 0; i < list.size(); i++) {
                    SearchItem item = list.get(i);
                    bulkRequest.add(new IndexRequest(ES_INDEX_NAME, ES_TYPE_NAME).
                            source(JsonUtils.objectToJson(item), XContentType.JSON));
                }
                restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                page++;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 按条件查询并返回
     *
     * @param q
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<SearchItem> selectByQ(String q, Long page, Integer pageSize) {
        try {
            SearchRequest searchRequest = new SearchRequest(ES_INDEX_NAME);
            searchRequest.types(ES_TYPE_NAME);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //1 查询名字 描述 卖点 类别包含Q的商品
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(q,
                    new String[]{"item_title", "item_desc", "item_sell_point", "item_category_name"}));
            //分页
            Long from = (page - 1) * pageSize;
            searchSourceBuilder.from(from.intValue());
            searchSourceBuilder.size(pageSize);
            //高亮
            HighlightBuilder builder = new HighlightBuilder();
            builder.preTags("<font color='red'>");
            builder.postTags("</font>");
            builder.field("item_title");
            searchSourceBuilder.highlighter(builder);
            //复杂的代码 先写需求
            searchRequest.source(searchSourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            //返回查询结果
            List<SearchItem> list = new ArrayList<>();
            for (int i = 0; i < hits.length; i++) {
                SearchHit hit = hits[i];
                SearchItem searchItem = JsonUtils.jsonToPojo(hit.getSourceAsString(), SearchItem.class);
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (highlightFields != null && highlightFields.size() > 0) {
                    searchItem.setItem_title(highlightFields.get("item_title").getFragments()[0].toString());
                }
                list.add(searchItem);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将指定 ID 商品查询并同步进索引
     *
     * @param itemId
     * @return
     * @throws IOException
     */

    @Override
    public int insertDocument(String itemId) throws IOException {
        SearchItem searchItem = searchItemMapper.getItemById(Long.valueOf(itemId));
        IndexRequest indexRequest = new IndexRequest(ES_INDEX_NAME, ES_TYPE_NAME);
        indexRequest.source(JsonUtils.objectToJson(searchItem), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return indexResponse.getShardInfo().getFailed();
    }

    /**
     * 判断数据库是否存在
     *
     * @return
     * @throws IOException
     */
    private boolean isExistsIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        GetIndexRequest indices = request.indices(ES_INDEX_NAME);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引库
     *
     * @return
     * @throws IOException
     */
    private boolean createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(ES_INDEX_NAME);
        request.settings(Settings.builder().put("number_of_shards", 2)
                .put("number_of_replicas", 1));
        request.mapping(ES_TYPE_NAME, "{\n" +
                "  \"_source\": {\n" +
                "    \"excludes\": [\n" +
                "      \"item_desc\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"properties\": {\n" +
                "    \"item_id\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"item_title\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    },\n" +
                "    \"item_sell_point\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    },\n" +
                "    \"item_price\": {\n" +
                "      \"type\": \"float\"\n" +
                "    },\n" +
                "    \"item_image\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"index\": false\n" +
                "    },\n" +
                "    \"item_category_name\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    },\n" +
                "    \"item_desc\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\",\n" +
                "      \"search_analyzer\": \"ik_smart\"\n" +
                "    }\n" +
                "  }\n" +
                "}", XContentType.JSON);
        //创建索引操作客户端
        IndicesClient indices = restHighLevelClient.indices();
        //创建响应对象
        CreateIndexResponse response = indices.create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

}
