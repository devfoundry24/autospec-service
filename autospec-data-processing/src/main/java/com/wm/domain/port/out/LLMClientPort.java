package com.wm.domain.port.out;

public interface LLMClientPort {

    /**
     * Uses a Large Language Model (LLM) to classify the product type based on the provided prompt.
     * @param prompt
     * @return
     */
    String getProductTypeFromLLM(String prompt);

}
