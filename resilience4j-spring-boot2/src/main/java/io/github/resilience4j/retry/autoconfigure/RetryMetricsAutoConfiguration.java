/*
 * Copyright 2019 Mahmoud Romeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.resilience4j.retry.autoconfigure;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.micrometer.RetryMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.retry.RetryRegistry;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} for resilience4j-metrics.
 */
@Configuration
@ConditionalOnClass(MetricsAutoConfiguration.class)
@AutoConfigureAfter(value = {RetryAutoConfiguration.class, MetricsAutoConfiguration.class})
@ConditionalOnProperty(value = "resilience4j.retry.metrics.enabled", matchIfMissing = true)
public class RetryMetricsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "resilience4j.retry.metrics.use_legacy_binder", havingValue = "true")
	public RetryMetrics registerLegacyRetryMetrics(RetryRegistry retryRegistry) {
		return RetryMetrics.ofRetryRegistry(retryRegistry);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(
			value = "resilience4j.retry.metrics.use_legacy_binder",
			havingValue = "false",
			matchIfMissing = true
	)
	public TaggedRetryMetrics registerRetryMetrics(RetryRegistry retryRegistry) {
		return TaggedRetryMetrics.ofRetryRegistry(retryRegistry);
	}
}
