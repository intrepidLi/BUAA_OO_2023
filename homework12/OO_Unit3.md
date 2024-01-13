# 面向对象第三单元总结
第三单元作业完成基本功能的难度并不大，只要理解好JML规格并对应完成即可，但较易出问题的测试点主要是对图论算法的考虑。我们要对图的规模，图的构造均有所考虑，在图会发生动态变化的情况下，如何既能正确实现功能，还能取得好的性能是本单元的训练目标。

## 分析本单元的测试过程
### 谈谈你对黑箱测试、白箱测试的理解
> **黑盒测试(Black-box testing)**，软件测试的主要方法之一，也可以称为功能测试、数据驱动测试或基于规格说明的测试。测试者不了解程序的内部情况，不需具备应用程序的代码、内部结构和编程语言的专门知识。只知道程序的输入、输出和系统的功能，这是从用户的角度针对软件界面、功能及外部结构进行测试，而不考虑程序内部逻辑结构。测试案例是依应用系统应该做的功能，照规范、规格或要求等设计。测试者选择有效输入和无效输入来验证是否正确的输出。
> 此测试方法可适合大部分的软件测试，如集成测试（integration testing）以及系统测试（system testing）。-- WikiPedia

简单来说，黑箱测试是一种只依靠输入，输出对程序正确性做出判断的测试方法，在课下的debug中我自己也主要使用了这种测试方法，我魔改了上届学长的数据生成器自己构造了一个对拍器，通过该对拍器和同学对拍直接定位BUG指令并通过不断重复该BUG指令修改输入，找到BUG具体位置。


> **白盒测试（white-box testing）** 又称透明盒测试（glass box testing）、结构测试（structural testing）等，软件测试的主要方法之一，也称结构测试、逻辑驱动测试或基于程序本身的测试。测试应用程式的内部结构或运作，而不是测试应用程式的功能（即黑箱测试）。在白箱测试时，以程式语言的角度来设计测试案例。测试者输入资料验证资料流在程式中的流动路径，并确定适当的输出，类似测试电路中的节点。测试者了解待测试程序的内部结构、算法等信息，这是从程序设计者的角度对程序进行的测试。
> 白箱测试可以应用于单元测试（unit testing）、整合测试（integration testing）和系统的软体测试流程，可测试在整合过程中每一单元之间的路径，或者主系统跟子系统中的测试。尽管这种测试的方法可以发现许多的错误或问题，它可能无法检测未使用部分的规范。 -- WiKiPedia

白箱测试是一种通过观察和理解代码寻找BUG的测试方法，在互测中通过观察房友的代码有时可以寻找到一些较明显的错误。比如用数组记录数据开的很大使得空间爆炸出现MLE，或者数组开的很小或者直接把PeronId作为数组的索引会直接出现RE。

### 对单元测试、功能测试、集成测试、压力测试、回归测试的理解

单元测试（英语：Unit Testing）又称为模块测试，是针对程序模块（软件设计的最小单位）来进行正确性检验的测试工作。程序单元是应用的最小可测试部件。在过程化编程中，一个单元就是单个程序、函数、过程等；对于面向对象编程，最小单元就是方法，包括基类（超类）、抽象类、或者派生类（子类）中的方法。

Functional testing（功能测试），也称为behavioral testing（行为测试），根据产品特性、操作描述和用户方案，测试一个产品的特性和可操作行为以确定它们满足设计需求。本地化软件的功能测试，用于验证应用程序或网站对目标用户能正确工作。使用适当的平台、浏览器和测试脚本，以保证目标用户的体验将足够好，就像应用程序是专门为该市场开发的一样。

集成测试又称组装测试，即对程序模块采用一次性或增值方式组装起来，对系统的接口进行正确性检验的测试工作。整合测试一般在单元测试之后、系统测试之前进行。实践表明，有时模块虽然可以单独工作，但是并不能保证组装起来也可以同时工作。

压力测试是给系统或项目不断加压，强制其在极限的情况下运行，观察它可以运行到何种程度，从而发现性能缺陷，是通过搭建与实际环境相似的测试环境，通过测试程序在同一时间内或某一段时间内，向系统发送预期数量的交易请求、测试系统在不同压力情况下的效率状况，以及系统可以承受的压力情况。在我们的作业中就可以包含对特殊指令的压力测试，比如第9次的`qts`，第10次的`qcs`，第11次的`qlm`等，我们可以先进行`ap`和`ar`，之后增加大量的相应指令进行压力测试。

回归测试(Regression Test)是指在软件项目中，开发人员在修改了软件的代码以修复已经发现的bug后，测试人员在需要重新测试前面已经测试过的内容，以确认此次修改没有引入新的错误。 也就是说，回归测试的目的就是检查开发人员在修复已有bug时是否又导致了新的bug。

## 是否使用测试工具

没有使用专门的测试工具，课程组推荐的jUnit并没有尝试。我是通过自己和同学的数据生成器和对拍器进行对拍进行对程序的测试。

## 数据构造的策略

总体来说是面向JML的数据构造，需要考虑几个容器包含关系以及邻接关系的不同情况。比如生成两个人personId的函数`get_double_person_id`中生成的两个`personId`的关系：
+ person1和person2都不存在
+ person1和person2只有一个存在
+ person1和person2都存在，且两个person不邻接
+ person1和person2都存在，且两个person邻接

对于多种情况要设置概率保证每种情况都会被覆盖。

## 架构设计及图模型构建和维护策略

本单元架构设计遵从JML规格即可，不需要过多考虑。

对于图模型的构建及维护，我主要采用的是并查集算法。在第11次作业中针对`qlm`也即无向图中最小环使用的最短路径树和并查集共同维护的算法。对于查找过无向图中某点最小环，我首先采用Dijkstra算法建立对于整个图的最短路径树，并在这个过程中记录每个子节点的根节点，对子树建立了一个并查集。通过遍历非树边查找该边两端点是否有相同的根节点且根节点为源点，并比较环的大小判断是否为过该源点的最小环。

## 性能及BUG修复

本单元作业中三次强测均出现了CTLE的问题，第十次作业是规格与实现未分离问题，而第九次和第十一次是是算法的选择与实现问题。
第九次作业强测中是由于并查集的算法出现问题，我没有在并查集查询过程中更新父节点，导致并查集每次查询的时间复杂度是$O(n)$，在对`qbs`指令进行处理时超时。
第十次作业强测中是`qcs`的实现是依照规格写的，导致三重循环时间复杂度过高超时。经过思考，我将该指令的实现与规格分离，大大降低了时间复杂度。
第十一次作业强测中是`qlm`指令，我在算法选择上出现了问题，本次作业对算法的时间复杂度要求极高，讨论区的诸多方法中实际只有两个能够通过强测中的压力测试。但遗憾的是我选择了删边`Dijkstra`算法，虽然对`Dijkstra`算法进行了优先队列优化，复杂度大约是$O(n^2logn)$，但是不能通过压力测试。在BUG修复中我采用了最短路径树+并查集的方法使复杂度降至低于$O(n^2)$，顺利通过了三个`qlm`压力测试点。

规格与实现分离是指在软件开发过程中将不同抽象级别的规格（如需求规格、接口规格等）与具体代码实现分开的做法。这样做使得规格和实现相互独立、互不影响。总体来说，本单元程序规格与实现的分离其实较易实现，比如`qts`等指令通过维护全局变量来实现，对于特别复杂的规格理解之后选择算法来解决。本单元的真正难点似乎放在了算法的选择与实现上，这深化了我对数据结构课和离散课上图论的理解，对一些算法进行了回顾。

## OKTest方法的理解

OKTest是一种检验代码实现与规格的一致性的方法。在三次作业中我们实现了三个不同的OKTest，用于检验方法是否满足了规格。该方法可用于对代码实现的测试中，对每个方法编写OKTest实际上可以以一种白盒形式检查代码是否符合了要求。

## 本单元学习体会

总的来说，本单元的投入时间与前两单元其实相差无几，这本身可能因为我的算法水平不足和对JML规格理解的不足。通过本单元的学习，我了解了JML规格对程序方法的指导性和规束性，同时也对算法进行了大量的回顾。同时由于本单元中测难度的大大降低，我极大地提高了自我寻找BUG的能力，通过数据生成器和对拍器与同学的程序进行对拍是我本单元每次作业都必须进行的一个过程，这也成功拯救了我的很多BUG。