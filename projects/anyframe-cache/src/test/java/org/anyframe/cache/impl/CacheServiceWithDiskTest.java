/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.anyframe.cache.impl;

import java.io.File;

import org.anyframe.cache.CacheService;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;



import com.opensymphony.oscache.base.NeedsRefreshException;

/**
 * For testing functions which Anyframe's CacheService supports, there are some
 * test scenarios in this TestCase. This cache should cache objects in disk.
 * 
 * @author SoYon Lim
 * @author JongHoon Kim
 */
public class CacheServiceWithDiskTest extends
		AbstractDependencyInjectionSpringContextTests {

	private CacheService cacheService;

	/**
	 * setter
	 * 
	 * @param cacheService
	 *            CacheService to be set
	 */
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	/**
	 * overrided
	 * 
	 * @return String[]
	 */
	protected String[] getConfigLocations() {
		return new String[] { "spring/context-disk-cache.xml" };
	}

	/**
	 * initialize test environment
	 */
	protected void onSetUp() {
		File file = new File("./temp/cache/application");
		File[] files = file.listFiles();

		if (files != null)
			for (int i = 0; i < files.length; i++)
				files[i].delete();
	}

	/**
	 * [Flow #-1] Positive Case : Using CacheService, put some contents and get
	 * a value by key and check number of files.
	 * 
	 * @throws Exception
	 *             fail to test
	 */
	public void testCacheWithDisk() throws Exception {

		// 1. Put some content in cache.
		for (int i = 0; i < 11; i++)
			cacheService.putInCache("KEY-" + i, "VALUE-" + i);

		// 2. Get one item from Cache.
		assertEquals("VALUE-10", cacheService.getFromCache("KEY-10"));

		// 3. Get one item from Cache which doesn't exist in current cache.
		// because maximum size of this cache is 10.
		try {
			cacheService.getFromCache("KEY-0");
			fail("fail to get a content from cache.");
		} catch (Exception e) {
			assertTrue(e instanceof NeedsRefreshException);
		}

		// 4. Check number of files. It must be equals maximum size of cache.
		File cachefiles = new File("./temp/cache/application");
		assertEquals(10, cachefiles.listFiles().length);
	}
}
