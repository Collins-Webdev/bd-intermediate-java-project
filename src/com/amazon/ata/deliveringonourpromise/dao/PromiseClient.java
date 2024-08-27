package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.types.Promise;

public interface PromiseClient {
    Promise getPromise(String customerOrderItemId);
}