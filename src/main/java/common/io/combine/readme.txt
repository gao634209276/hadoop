在使用Hadoop处理海量小文件的应用场景中，如果你选择使用CombineFileInputFormat，而且你是第一次使用，可能你会感到有点迷惑。
虽然，从这个处理方案的思想上很容易理解，但是可能会遇到这样那样的问题。

使用CombineFileInputFormat作为Map任务的输入规格描述，首先需要实现一个自定义的RecordReader。
CombineFileInputFormat的大致原理是，
	他会将输入多个数据文件（小文件）的元数据全部包装到CombineFileSplit类里面。
	也就是说，因为小文件的情况下，在HDFS中都是单Block的文件，即一个文件一个Block，
	一个CombineFileSplit包含了一组文件Block，包括每个文件的起始偏移（offset），长度（length），Block位置（localtions）等元数据。
	如果想要处理一个CombineFileSplit，很容易想到，对其包含的每个InputSplit
	（实际上这里面没有这个，你需要读取一个小文件块的时候，需要构造一个FileInputSplit对象）。
在执行MapReduce任务的时候，需要读取文件的文本行（简单一点是文本行，也可能是其他格式数据）。
	那么对于CombineFileSplit来说，你需要处理其包含的小文件Block，
	就要对应设置一个RecordReader，才能正确读取文件数据内容。
	通常情况下，我们有一批小文件，格式通常是相同的，
	只需要在为CombineFileSplit实现一个RecordReader的时候，
	内置另一个用来读取小文件Block的RecordReader，
	这样就能保证读取CombineFileSplit内部聚积的小文件。

编程实现

通过上面的说明，我们基于Hadoop内置的CombineFileInputFormat来实现处理海量小文件，需要做的工作就很显然了，如下所示：

	实现一个RecordReader来读取CombineFileSplit包装的文件Block
	继承自CombineFileInputFormat实现一个使用我们自定义的RecordReader的输入规格说明类
	处理数据的Mapper实现类
	配置用来处理海量小文件的MapReduce Job

下面，对编程实现的过程，详细讲解：
	为CombineFileSplit实现一个RecordReader，
	并在内部使用Hadoop自带的LineRecordReader来读取小文件的文本行数据，代码实现如下所示：
	See: CombineSmallfileRecordReader类


如果存在这样的应用场景，你的小文件具有不同的格式，
	那么久需要考虑对不同类型的小文件，使用不同的内置RecordReader，
	具体逻辑也是在上面的类中实现。
    CombineSmallfileInputFormat类

我们已经为CombineFileSplit实现了一个RecordReader，
	然后需要在一个CombineFileInputFormat中注入这个RecordReader类实现类CombineSmallfileRecordReader的对象。
	这时，需要实现一个CombineFileInputFormat的子类，可以重写createRecordReader方法。
	我们实现的CombineSmallfileInputFormat，代码如下所示：
	See:CombineSmallfileInputFormat
	上面比较重要的是，一定要通过CombineFileRecordReader来创建一个RecordReader，
		而且它的构造方法的参数必须是上面的定义的类型和顺序，
		构造方法包含3个参数：
			第一个是CombineFileSplit类型，
			第二个是TaskAttemptContext类型，
			第三个是Class<? extends RecordReader>类型。
   
 CombineSmallfileMapper类
下面，我们实现我们的MapReduce任务实现类，CombineSmallfileMapper类代码，如下所示：
比较简单，就是将输入的文件文本行拆分成键值对，然后输出。

CombineSmallfiles类
下面看我们的主方法入口类，这里面需要配置我之前实现的MapReduce Job，实现代码如下所示：


http://shiyanjun.cn/archives/299.html