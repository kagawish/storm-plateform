package testTridentFunctions;

        import backtype.storm.tuple.Values;
        import inputClass.Input;
        import org.apache.hadoop.conf.Configuration;
        import org.apache.hadoop.hbase.HBaseConfiguration;
        import otherClass.HBaseDB;
        import otherClass.Restaurant;
        import storm.trident.operation.BaseFunction;
        import storm.trident.operation.TridentCollector;
        import storm.trident.operation.TridentOperationContext;
        import storm.trident.tuple.TridentTuple;

        import java.io.IOException;
        import java.util.*;

/**
 * Created by asmaafillatre on 25/01/2016.
 */


public class testFunction extends BaseFunction {

    private int partitionIndex;
    private String msg;

    public testFunction(String msg){
        this.msg = msg;
    }

    @Override
    public void prepare(Map conf, TridentOperationContext context) {
        this.partitionIndex = context.getPartitionIndex();
    }

    public void execute(TridentTuple tuple, TridentCollector collector) {
            System.out.println(msg+" Partition "+partitionIndex+ " voici mon tuple : "+tuple.getString(0));
            collector.emit(new Values(tuple.getString(0)+msg));
    }
}