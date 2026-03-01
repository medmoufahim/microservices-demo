package com.microservices.demo.elastic.query.client.util;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.microservices.demo.elastic.model.index.IndexModel;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ElasticQueryUtil<T extends IndexModel> {

    public Query getSearchQueryById(String id) {
        return new NativeQueryBuilder()
                .withIds(Collections.singleton(id))
                .build();
    }

    public Query getSearchQueryByFieldText(String field, String text) {
        return new NativeQueryBuilder()
                .withQuery(QueryBuilders.bool(b -> b
                        .must(m -> m.match(mq -> mq
                                .field(field)
                                .query(text)))))
                .build();
    }

    public Query getSearchQueryForAll() {
        return new NativeQueryBuilder()
                .withQuery(QueryBuilders.matchAll(m -> m))
                .build();
    }
}
