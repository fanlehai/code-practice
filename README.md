# HadoopExercise


hadoop
	- RunDrivers : 用于快速调用下面程序(简化下面包名)
	- minmaxcount/MinMaxCountDriver : 计算数据中时间的最大值、最小值以及记录数；
	- sample/RandomSampleDriver : 获取一个大文件的子集，用于数据分析；
	- sort
		- TotalOrderSort : 根据key对文件进行全排序（使用多个reduce）；
		- TotalOrderSortSlow : 根据key对文件进行全排序(只使用一个reduce)；
		