import backtype.storm.spout.SchemeAsMultiScheme;
import kafka.api.OffsetRequest;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import bolts.InputCompareToDB;
import bolts.InputNormalizer;
import otherClass.MyConstants;
import storm.kafka.*;


public class TopologyMain {

        public static void main(String[] args) throws InterruptedException {
                TopologyBuilder builder=new TopologyBuilder();

                ZkHosts zkHosts = new ZkHosts("localhost:1984");

                String topic = MyConstants.TOPIC_NAME;
                String consumer_group_id = "id7";
                SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, topic, "", consumer_group_id);

                kafkaConfig.startOffsetTime = OffsetRequest.EarliestTime();
                kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

                KafkaSpout kafkaSpout = new KafkaSpout(kafkaConfig);
                builder.setSpout("KafkaSpout", kafkaSpout, MyConstants.NUM_PARTITIONS);
                builder.setBolt("input-normalizer", new InputNormalizer()).shuffleGrouping("KafkaSpout");
                builder.setBolt("input-compareToDB", new InputCompareToDB(), 1).fieldsGrouping("input-normalizer", new Fields("inputcoord"));

                Config conf;
                conf = new Config();
                conf.setDebug(false);

                conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
                LocalCluster cluster = new LocalCluster();

                System.err.println("START!!!!!!!!!!!!!!!!!!!!");

                cluster.submitTopology("Getting-Started-Topology", conf, builder.createTopology());


                Thread.sleep(600000);
                cluster.shutdown();

                System.err.println("END!!!!!!!!!!!!!!!!!!!!");
	}
}
