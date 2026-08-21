package com.externalapicalling.repository;

import com.externalapicalling.models.ProductEmbedding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductEmbeddingRepository
        extends JpaRepository<ProductEmbedding, Long> {
   @Query(value = """
                    SELECT * FROM product_embeddings ORDER BY embedding <=> CAST(:queryVector as vector)
                    LIMIT 3
                    """,nativeQuery = true)
    List<ProductEmbedding> findSimilarProducts(@Param("queryVector") String queryVector);

    boolean existsByProductName(String products);
}
