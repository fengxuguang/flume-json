## 过滤器插件使用
```properties
a1.sources = r1
a1.sinks = k1
a1.channels = c1

a1.sources.r1.type = org.apache.flume.source.kafka.KafkaSource
a1.sources.r1.channels = c1
a1.sources.r1.batchSize = 3
a1.sources.r1.batchDurationMillis = 2000
a1.sources.r1.kafka.bootstrap.servers = 192.162.0.1:9000
a1.sources.r1.kafka.topics = flume-source-kafka
a1.sources.r1.interceptors = i1 i2
a1.sources.r1.interceptors.i1.type = static
a1.sources.r1.interceptors.i1.key = topic
a1.sources.r1.interceptors.i1.preserveExisting = false
a1.sources.r1.interceptors.i1.value = flume-collect-kafka
# 配置自定义拦截器，类型必须是: 类全名$内部类名
a1.sources.r1.interceptors.i2.type = com.bda.dcp.flume.interceptor.JsonInterceptor$Builder
# 配置要过滤的字段
a1.sources.r1.interceptors.i2.jsonTopic=name,age

a1.sinks.k1.channel = c1
a1.sinks.k1.type = org.apache.flume.sink.kafka.KafkaSink
a1.sinks.k1.kafka.flumeBatchSize = 3
a1.sinks.k1.kafka.bootstrap.servers = 192.162.0.1:9000
a1.sinks.k1.kafka.topic = flume-collect-kafka
a1.sinks.k1.kafka.producer.acks = 1
a1.sinks.k1.kafka.producer.linger.ms = 1
a1.sinks.k1.kafka.producer.compression.type = snappy

a1.channels.c1.type = org.apache.flume.channel.kafka.KafkaChannel
a1.channels.c1.kafka.bootstrap.servers = 192.162.0.1:9000
a1.channels.c1.kafka.topic = flume-channel
a1.channels.c1.kafka.consumer.auto.offset.reset = latest
```