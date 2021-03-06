package domain.service.algOutput;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import domain.core.algInputDataProcess.DataColumn;
import domain.core.algInputDataProcess.DataRow;
import domain.core.algInputDataProcess.DataSet;
import domain.core.algOutput.FittedValue;
import domain.core.algOutput.Gene;
import domain.core.algOutput.GenePiece;
import domain.core.algOutput.GenePieceType;
import domain.core.algOutput.GeneType;
import domain.core.algOutput.GepAlgRun;
import domain.core.algOutput.Individual;
import domain.core.algOutput.Population;
import domain.core.algconfiguration.Function;
import domain.core.algconfiguration.GeneConfiguration;
import domain.core.algconfiguration.GepAlgConfiguration;
import domain.core.algconfiguration.IndividualConfiguration;
import domain.core.algconfiguration.OperatorConfiguration;
import domain.iservice.algOutput.IAlgRunStep;
import domain.iservice.algOutput.IGenerator;

public class AlgCpuRunStep implements IAlgRunStep {
	private IGenerator generator;
	enum TransportEnum{
		IS,RIS,GENE;
		float rate;
		Integer[] transportElement;
		public void setRate(float rate){
			this.rate=rate;
		}
		public float getRate(){
			return rate;
		}
		public void setTransportElement(Integer[] transportElement){
			this.transportElement=transportElement;
		}
		public Integer[] getTransportElement(){
			return transportElement;
		}
	}
	enum Recombine{
		OnePoint,TwoPoint,GENE;
		float rate;
		public void setRate(float rate){
			this.rate=rate;
		}
		public float getRate(){
			return rate;
		}
	}
	@Override
	public GepAlgRun create(GepAlgConfiguration gepAlgConfiguration, DataSet dataSet) {
		// TODO Auto-generated method stub
		Individual individual;
		Gene addedGene;
		Population population;
		FittedValue fittedValue;
		List<FittedValue> fittedValues;
		GeneConfiguration geneConfiguration=gepAlgConfiguration.getIndividualConfiguration().getGeneConfiguration();
		int dataColumns=dataSet.getVariableUsed().size();
		GepAlgRun gepAlgRun=generateGepAlgRun(dataSet, gepAlgConfiguration);
		population=gepAlgRun.getCurrentPopulation();
		generator=new Generator(geneConfiguration.getFunctionUsed(), dataSet.getVariableUsed(), geneConfiguration.getNormalGeneNumber());
		for(int i=0;i<gepAlgConfiguration.getIndividualConfiguration().getIndividualNumber();i++){
			individual=new Individual();
			individual.setGenes(new ArrayList<Gene>(gepAlgConfiguration.getIndividualConfiguration().getTotalGeneNumbers()));
			for(int j=0;j<geneConfiguration.getNormalGeneNumber();j++){
				addedGene=nextGene(GeneType.NormalGene, geneConfiguration, dataColumns);
				individual.getGenes().add(addedGene);
			}
			for(int j=0;j<geneConfiguration.getHomeoticGeneNumber();j++){
				addedGene=nextGene(GeneType.HomeoticGene, geneConfiguration, dataColumns);
				individual.getGenes().add(addedGene);
			}
			fittedValues=new ArrayList<FittedValue>(dataSet.getRowNum());
			individual.setFittedValues(fittedValues);
			for(int j=0;j<dataSet.getRowNum();j++){
				fittedValue=new FittedValue();
				fittedValue.setDataRow(dataSet.getDataRows().get(j));
				fittedValues.add(fittedValue);
			}
			population.addIndividual(individual);
		}
		return gepAlgRun;
	}
	@Override
	public List<Float> calculateFitness(Population population) {
		// TODO Auto-generated method stub
		GepAlgConfiguration gepAlgConfiguration=population.getGepAlgRun().getGepAlgConfiguration();
		GeneConfiguration geneConfiguration=gepAlgConfiguration.getIndividualConfiguration().getGeneConfiguration();
		int homeoticGeneNum=geneConfiguration.getHomeoticGeneNumber();
		DataSet dataSet=population.getGepAlgRun().getDataSet();
		List<Float> individualFitness=new ArrayList<Float>();
		float[] sumFitness=new float[homeoticGeneNum];
		float[][] individualsValues=new float[dataSet.getRowNum()][];
		float[] oneRowFitnesses;
		int bestHomeoticIndex;
		
		for(Individual individual:population.getIndividuals()){
			Arrays.fill(sumFitness, 0);
			for(int i=0;i<dataSet.getRowNum();i++){
				clearFunctionFlag(individual);
				individualsValues[i]=calcFittedValue(individual, dataSet.getDataRows().get(i), geneConfiguration);
				oneRowFitnesses=calcFitness(individualsValues[i], dataSet.getDataRows().get(i).getResultColumn(),gepAlgConfiguration);
				sumFitness=addToSumFitness(sumFitness, oneRowFitnesses);
			}
			bestHomeoticIndex=findBestHomeoticIndex(sumFitness);
			setIndividual(bestHomeoticIndex, individual, individualsValues, sumFitness,dataSet);
			individualFitness.add(individual.getFitness());
		}
		return individualFitness;
	}

	
	@Override
	public Population select(GepAlgRun gepAlgRun) {
		// TODO Auto-generated method stub
		Population lastPopulation=gepAlgRun.getCurrentPopulation();
		float sumFitness=addFitness(lastPopulation);
		List<Float> probability=calculateProbability(lastPopulation, sumFitness);
		calculateCumulative(probability);
		Population newPopulation=createNewPopulation(gepAlgRun.getPopulations(), probability);
		return newPopulation;
	}

	@Override
	public boolean mutate(Population population) {
		// TODO Auto-generated method stub
		GeneConfiguration geneConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getIndividualConfiguration().getGeneConfiguration();
		int functionListSize=geneConfiguration.getFunctionUsed().size();
		int maxNormalGeneType=functionListSize+population.getGepAlgRun().getDataSet().getVariableUsed().size();
		int maxHomeoticGeneType=functionListSize+geneConfiguration.getNormalGeneNumber();
		float mutateRate=population.getGepAlgRun().getGepAlgConfiguration().getOperatorConfiguration().getMutateRate();
		GenePiece mutatedGenePiece;
		Random mutateRandom=new Random();
		for(Individual mutatingIndividual:population.getIndividuals()){
			for(Gene gene:mutatingIndividual.getGenes()){
				if(gene.getGeneType()==GeneType.NormalGene){
					for(int i=0;i<geneConfiguration.getNormalGeneHeaderLength();i++){
						if(mutateRandom.nextFloat()<mutateRate){
							mutatedGenePiece=nextGenePiece(GeneType.NormalGene, true, maxNormalGeneType, functionListSize);
							gene.getGenePieces().set(i, mutatedGenePiece);
						}
					}
					for(int i=0;i<geneConfiguration.getNormalGeneTailLength();i++){
						if(mutateRandom.nextFloat()<mutateRate){
							mutatedGenePiece=nextGenePiece(GeneType.NormalGene, false, maxNormalGeneType, functionListSize);
							gene.getGenePieces().set(i+geneConfiguration.getNormalGeneHeaderLength(),mutatedGenePiece);
						}
					}
				}
				else if(gene.getGeneType()==GeneType.HomeoticGene) {
					if(geneConfiguration.getUseHomeoticGene()){
						if(mutateRandom.nextFloat()<mutateRate){
							mutatedGenePiece=nextGenePiece(GeneType.HomeoticGene, true, maxHomeoticGeneType, functionListSize);
							gene.getGenePieces().set(0, mutatedGenePiece);
						}
						for(int i=1;i<geneConfiguration.getHomeoticGeneHeaderLength();i++){
							if(geneConfiguration.getUseHomeoticGene()){
								mutatedGenePiece=nextGenePiece(GeneType.HomeoticGene, true, maxHomeoticGeneType, functionListSize);
								gene.getGenePieces().set(i, mutatedGenePiece);
							}
						}
						for(int i=0;i<geneConfiguration.getHomeoticGeneTailLength();i++){
							if(geneConfiguration.getUseHomeoticGene()){
								mutatedGenePiece=nextGenePiece(GeneType.HomeoticGene, false, maxHomeoticGeneType, functionListSize);
								gene.getGenePieces().set(i+geneConfiguration.getHomeoticGeneHeaderLength(), mutatedGenePiece);
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean isTransport(Population population) {
		// TODO Auto-generated method stub
		OperatorConfiguration operatorConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getOperatorConfiguration();
		TransportEnum isTransportEnum=TransportEnum.IS;
		isTransportEnum.setRate(operatorConfiguration.getIsTransportRate());
		isTransportEnum.setTransportElement(operatorConfiguration.getIsElement());
		iterateGeneInTransport(population, isTransportEnum);
		return true;
	}

	@Override
	public boolean risTransport(Population population) {
		// TODO Auto-generated method stub
		OperatorConfiguration operatorConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getOperatorConfiguration();
		TransportEnum risTransportEnum=TransportEnum.RIS;
		risTransportEnum.setRate(operatorConfiguration.getRisTransportRate());
		risTransportEnum.setTransportElement(operatorConfiguration.getRisElement());
		iterateGeneInTransport(population, risTransportEnum);
		return true;
	}

	@Override
	public boolean geneTransport(Population population) {
		// TODO Auto-generated method stub
		OperatorConfiguration operatorConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getOperatorConfiguration();
		TransportEnum geneTransportEnum=TransportEnum.GENE;
		geneTransportEnum.setRate(operatorConfiguration.getGeneTransportRate());
		iterateGeneInTransport(population, geneTransportEnum);
		return true;
	}

	@Override
	public boolean onePointRecombine(Population population) {
		// TODO Auto-generated method stub
		OperatorConfiguration operatorConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getOperatorConfiguration();
		Recombine recombine=Recombine.OnePoint;
		recombine.setRate(operatorConfiguration.getOnePointRecombineRate());
		iterateGeneInRecombine(population.getGepAlgRun().getGepAlgConfiguration().getIndividualConfiguration(), population, recombine);
		return true;
	}

	@Override
	public boolean twoPointRecombine(Population population) {
		// TODO Auto-generated method stub
		OperatorConfiguration operatorConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getOperatorConfiguration();
		Recombine recombine=Recombine.TwoPoint;
		recombine.setRate(operatorConfiguration.getTwoPointRecombineRate());
		iterateGeneInRecombine(population.getGepAlgRun().getGepAlgConfiguration().getIndividualConfiguration(), population, recombine);
		return true;
	}

	@Override
	public boolean geneRecombine(Population population) {
		// TODO Auto-generated method stub
		OperatorConfiguration operatorConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getOperatorConfiguration();
		Recombine recombine=Recombine.GENE;
		recombine.setRate(operatorConfiguration.getGeneRecombineRate());
		iterateGeneInRecombine(population.getGepAlgRun().getGepAlgConfiguration().getIndividualConfiguration(), population, recombine);
		return true;
	}
	private GepAlgRun generateGepAlgRun(DataSet dataSet,GepAlgConfiguration gepAlgConfiguration){
		GepAlgRun gepAlgRun=new GepAlgRun();
		gepAlgRun.setDataSet(dataSet);
		gepAlgRun.setGepAlgConfiguration(gepAlgConfiguration);
		Population population=new Population();
		population.setIndividuals(new ArrayList<Individual>(gepAlgConfiguration.getIndividualConfiguration().getIndividualNumber()));
		population.setGenerationNum((long) 0);
		population.setGepAlgRun(gepAlgRun);
		gepAlgRun.getPopulations().add(population);
		return gepAlgRun;
	}
	private Gene nextGene(GeneType geneType,GeneConfiguration geneConfiguration,int dataColumns){
		int geneLength;
		int headerLength;
		int tailLength;
		int maxType;
		GenePiece genePiece=null;
		int j;
		if(geneType==GeneType.NormalGene){
			geneLength=geneConfiguration.getNormalGeneLength();
			headerLength=geneConfiguration.getNormalGeneHeaderLength();
			tailLength=geneConfiguration.getNormalGeneTailLength();
			maxType=dataColumns+geneConfiguration.getFunctionUsed().size();
			j=0;
		}
		else {
			geneLength=geneConfiguration.getHomeoticGeneLength();
			headerLength=geneConfiguration.getHomeoticGeneHeaderLength();
			tailLength=geneConfiguration.getHomeoticGeneTailLength();
			maxType=geneConfiguration.getFunctionUsed().size()+geneConfiguration.getNormalGeneNumber();
			j=1;
			if(geneConfiguration.getUseHomeoticGene())
				genePiece=generator.nextFunction();
			else
				genePiece=generator.nextFunction(geneConfiguration.getConnectionFunction());
		}
		Gene result=new Gene();
		result.setGeneType(geneType);
		List<GenePiece> genePiecesList=new ArrayList<GenePiece>(geneLength);
		if(genePiece!=null)
			genePiecesList.add(genePiece);
		for(;j<headerLength;j++){
			if(!geneConfiguration.getUseHomeoticGene()&&geneType==GeneType.HomeoticGene)
				genePiece=generator.nextFunction(geneConfiguration.getConnectionFunction());
			else
				genePiece=nextGenePiece(geneType, true, maxType,geneConfiguration.getFunctionUsed().size());
			genePiecesList.add(genePiece);
		}
		for(j=0;j<tailLength;j++){
			if(!geneConfiguration.getUseHomeoticGene()&&geneType==GeneType.HomeoticGene)
				genePiece=generator.nextNoramlGeneNum(j);
			else
				genePiece = nextGenePiece(geneType, false, maxType,geneConfiguration.getFunctionUsed().size());
			genePiecesList.add(genePiece);
		}
		result.setGenePieces(genePiecesList);
		return result;
	}
	private GenePiece nextGenePiece(GeneType geneType, boolean header,int maxType,int functionListSize) {
		// TODO Auto-generated method stub
		Random typeRandom=new Random();
		GenePiece genePiece;
		int type;
		if(geneType==GeneType.NormalGene){
			if(header){
				type=typeRandom.nextInt(maxType);
				if(type<functionListSize)
					genePiece=generator.nextFunction();
				else
					genePiece=generator.nextVariable();
			}
			else {
				genePiece=generator.nextVariable();
			}
		}
		else {
			if(header){
				type=typeRandom.nextInt(maxType);
				if(type<functionListSize)
					genePiece=generator.nextFunction();
				else
					genePiece=generator.nextNoramlGeneNum();
			}
			else {
				genePiece=generator.nextNoramlGeneNum();
			}
		}
		return genePiece;
	}
	/**
	 * 因为每一个个体有多个同源基因，因此每一个同源基因都可以产生一个拟合值，这里返回一个包含个体数组，里面每个同源基因计算出的拟合值。
	 * 目前是性能瓶颈，自用时间在15%－20%
	 * @param individual 待计算的个体
	 * @param row 需要产生拟合值的行
	 * @return 拟合值数组
	 */
	private float [] calcFittedValue(Individual individual,DataRow row,GeneConfiguration geneConfiguration){
		int normalGeneNum=geneConfiguration.getNormalGeneNumber();
		int homeoticGeneNum=geneConfiguration.getHomeoticGeneNumber();
		for(int i=0;i<normalGeneNum;i++){
			assignValueToVariable(individual.getGenes().get(i), row);
			calculateGeneValue(individual.getGenes().get(i), individual);
		}
		float result;
		float[] resultList=new float[homeoticGeneNum];
		if(geneConfiguration.getNormalGeneNumber()==1&&geneConfiguration.getHomeoticGeneNumber()==1){
			resultList[0]=individual.getGenes().get(0).getValue();
		}
		else {
			for(int i=0;i<homeoticGeneNum;i++){
				result=calculateGeneValue(individual.getGenes().get(i+normalGeneNum), individual);
				resultList[i]=result;		
			}			
		}
		return resultList;
	}
	
	
	/**
	 * 本方法给基因中的有效长度内的每一个变量赋值
	 * @param gene 待赋值的基因
	 * @param row 一个保存着变量值的输入行
	 */
	private void assignValueToVariable(Gene gene, DataRow row) {
		// TODO Auto-generated method stub
		GenePiece genePiece;
		int effectiveLength=gene.getEffictiveLength();
		List<GenePiece> genePieces=gene.getGenePieces();
		List<DataColumn> dataColumns=row.getDataColumns();
		for(int i=0;i<effectiveLength;i++){
			if((genePiece=genePieces.get(i)).getGenePieceType()==GenePieceType.Variable){
				genePiece.setValue(dataColumns.get(genePiece.getVariableIndex()).getValue());
			}
		}
	}
	/**
	 * 计算给定基因的值，这个基因可能是普通基因，也可能是同源基因，并将结果保存到到基因对应的属性里面
	 * @param gene 待计算的基因
	 * @param individual 基因所属的个体
	 * @return 基因的值，这个结果同样也会被保存到基因对应的属性里面
	 */
	private float calculateGeneValue(Gene gene, Individual individual) {
		// TODO Auto-generated method stub
		int length=gene.getEffictiveLength();
		GenePiece lastNonTerminate;
		int arity;
		while(length>1){
			lastNonTerminate=gene.getGenePieces().get(gene.getLastNonTerminate(length));
			arity=lastNonTerminate.getFunc().getArity();
			execMathFunction(gene, individual,lastNonTerminate,length);
			length-=arity;
		}
		float value=gene.getGenePieces().get(0).getValue();
		gene.setValue(value);
		return value;
	}
	/**
	 * 执行某个数学函数，并返回求解的结果
	 * @param gene 待执行的函数所在的基因
	 * @param individual 待执行的函数所在的个体
	 * @param computedGenePiece 待执行的函数所在基因位
	 * @param efficientLength 有效长度
	 * @return 数学函数计算出的结果
	 */
	private float execMathFunction(Gene gene,Individual individual,GenePiece computedGenePiece,int efficientLength){
		Function function=computedGenePiece.getFunc();
		int arity=function.getArity();
		float[] parameterArray=new float[arity];
		GenePiece[] genePieces=new GenePiece[arity];
		for(int i=0;i<arity;i++)
			genePieces[i]=gene.getGenePieces().get(efficientLength-i-1);
		if(gene.getGeneType()==GeneType.HomeoticGene){
			int index;
			GenePiece genePiece;
			for(int i=0;i<genePieces.length;i++){
				genePiece=genePieces[genePieces.length-1-i];
				if(genePiece.getGenePieceType()==GenePieceType.Function)
					parameterArray[i]=genePiece.getValue();
				else {
					index=(int)(float)genePiece.getValue();
					parameterArray[i]=individual.getGenes().get(index).getValue();
				}
			}		
		}
		else {
			for(int i=0;i<genePieces.length;i++)
				parameterArray[i]=genePieces[genePieces.length-1-i].getValue();
		}
		float value=function.operate(parameterArray);
		computedGenePiece.setValue(value);
		computedGenePiece.setUsed(true);
		return value;
	}

	/**
	 * 本方法负责从拟合值到适应值的转换，因为存在同源基因，所以一个个体对应多个拟合值，因此一个个体也对应多个适应值，这里只计算适应值矩阵的一行
	 * @param individualsValues 拟合值数组
	 * @param y 保存着函数值的列
	 * @return 得到的适应值矩阵中的某一行
	 */
	private float[] calcFitness(float[] individualsValues,DataColumn y,GepAlgConfiguration gepAlgConfiguration){
		float[] result=new float[individualsValues.length];
		float minus;
		float abs;
		for(int i=0;i<result.length;i++){
			minus=individualsValues[i]-y.getValue();
			abs=Math.abs(minus);
			if(abs<gepAlgConfiguration.getAccuracy())
				abs=0;
			result[i]=gepAlgConfiguration.getSelectionRange()-abs;
		}
		return result;
	}
	/**
	 * 本方法负责对适应值矩阵的每一列求和，最后得到一个行矩阵，保存这适应值矩阵中每列的和
	 * @param sumFitness 保存和的数组
	 * @param rowValue 某一行的适应值
	 * @return 求和后的数组
	 */
	private float[] addToSumFitness(float[] sumFitness,float[] rowValue){
		for(int i=0;i<sumFitness.length;i++)
			sumFitness[i]=rowValue[i]+sumFitness[i];
		return sumFitness;
	}
	/**
	 * 本方法负责清楚所有函数上的已使用的标记
	 * @param individual 待清除个体
	 */
	private void clearFunctionFlag(Individual individual){
		int geneNum=individual.getGenes().size();
		List<Gene> genes=individual.getGenes();
		Gene gene;
		for(int i=0;i<geneNum;i++){
			gene=genes.get(i);
			gene.clearFunctionFlag(false);
		}
	}
	/**
	 * 本方法求指定适应度矩阵行的最大值，并返回最大值所在列的索引
	 * @param sumFitness
	 * @return 最大适应值所在的列，若所有适应值均不合法，则返回－1
	 */
	private int findBestHomeoticIndex(float[] sumFitness){
		int k=-1;
		float value=-1;
		float fitness;
		for(int i=0;i<sumFitness.length;i++){
			fitness=sumFitness[i];
			if(Float.isInfinite(fitness)||Float.isInfinite(fitness)||fitness<=0)
				continue;
			else if(fitness>value){
				value=fitness;
				k=i;
			}		
		}

		return k;
	}
	/**
	 * 本方法将设置个体被选择的同源基因，个体的适应值，个体的拟合值
	 * @param index 最佳适应值所在的列
	 * @param individual 待设置个体
	 * @param individualVaule 拟合值矩阵
	 * @param fitness 适应值行
	 * @param dataSet 输入数据集
	 * @return
	 */
	private Individual setIndividual(int index,Individual individual,float[][] individualVaule,float[] fitness,DataSet dataSet){
		if(index==-1){
			individual.setFitness((float) 0);
			individual.setSelectedHomeoticGeneNumber(-1);
			for(int i=0;i<individual.getFittedValues().size();i++)
				individual.getFittedValues().get(i).setFittedValue((float) 0);
		}
		else {
			individual.setFitness(fitness[index]);
			individual.setSelectedHomeoticGeneNumber(index);
			for(int i=0;i<individualVaule.length;i++)
				individual.getFittedValues().get(i).setFittedValue(individualVaule[i][index]);

		}
		return individual;
	}
	/**
	 * 计算种群的总适应值
	 * @param population 待计算的种群
	 * @return 种群总适应值
	 */
	private float addFitness(Population population){
		float sum=0;
		for(Individual individual:population.getIndividuals())
			sum+=individual.getFitness();
		return sum;
	}
	/**
	 * 计算个体比例适应值，即每个个体在种群适应值中所占的比例
	 * @param population 待计算种群
	 * @param sum 种群适应值之和
	 * @return 保存有个体比例适应值的List
	 */
	private List<Float> calculateProbability(Population population,float sum){
		float divide;
		List<Float> floatList=new ArrayList<Float>(population.getIndividuals().size());
		for(Individual individual:population.getIndividuals()){
			divide=individual.getFitness()/sum;
			floatList.add(divide);
		}
		return floatList;
	}
	/**
	 * 计算个体累加适应值，即第n项的累加适应值等于前n项个体比例适应值之和
	 * @param probability 个体比例适应值之和
	 * @return 个体累加适应值
	 */
	private List<Float> calculateCumulative(List<Float> probability){
		float sum=0;
		for(int i=0;i<probability.size();i++){
			sum+=probability.get(i);
			probability.set(i, sum);
		}
		return probability;
	}
	/**
	 * 创建一个新的种群，使用轮盘赌+冠军策略
	 * @param original 原始种群
	 * @param cumulativeProbability 个体累加适应值
	 * @return 新的种群
	 */
	private Population createNewPopulation(List<Population> pList,List<Float> cumulativeProbability){
		Population original=pList.get(0);
		Population resultPopulation=new Population(original.getIndividuals().size());
		Individual bestIndividual=Collections.max(Arrays.asList(pList.get(pList.size()-1).getBestIndividual(),original.getBestIndividual())).clone();
		resultPopulation.addIndividual(bestIndividual);
		Random random=new Random();
		int position;
		Individual insertedIndividual;
		for(int i=1;i<cumulativeProbability.size();i++){
			position=search(cumulativeProbability, random.nextFloat());
			insertedIndividual=original.getIndividuals().get(position).clone();
			resultPopulation.addIndividual(insertedIndividual);
		}
		resultPopulation.setGepAlgRun(original.getGepAlgRun());
		return resultPopulation;
	}
	/**
	 * 查找在累加适应值中，某个小数值具体属于哪个范围，返回范围的下限（即使擦到上限）
	 * @param floats 个体累加适应值
	 * @param random 待查询的小数
	 * @return 累加适应值所代表的个体的index
	 */
	private int search(List<Float> floats,float random){
		for(int i=0;i<floats.size();i++){
			if(random<=floats.get(i))
				return i;
		}
		return floats.size()-1;
	}
	/**
	 * 遍历每个基因，决定是否进行转座，如果进行转座，则调用下一级函数
	 * @param population 要进行转座的种群
	 * @param transportEnum 转座枚举类
	 */
	private void iterateGeneInTransport(Population population,TransportEnum transportEnum){
		Random transportRandom=new Random();
		GeneConfiguration geneConfiguration=population.getGepAlgRun().getGepAlgConfiguration().getIndividualConfiguration().getGeneConfiguration();
		int headerLength;
		int tailLength;
		Gene gene;
		for(Individual individual:population.getIndividuals()){
			for(int i=0;i<individual.getGenes().size();i++){
				gene=individual.getGenes().get(i);
				if(transportRandom.nextFloat()<transportEnum.getRate()){
					if(transportEnum==TransportEnum.GENE){
						if(gene.getGeneType()==GeneType.HomeoticGene)
							break;
						else {
							transportParaDetermination(individual.getGenes(), i);
							break;
						}
					}
					else {
						if(gene.getGeneType()==GeneType.NormalGene){
							headerLength=geneConfiguration.getNormalGeneHeaderLength();
							tailLength=geneConfiguration.getNormalGeneTailLength();
						}
						else {
							if(geneConfiguration.getUseHomeoticGene()){
								headerLength=geneConfiguration.getHomeoticGeneHeaderLength();
								tailLength=geneConfiguration.getHomeoticGeneTailLength();
							}
							else {
								break;
							}
						}
						transportParaDetermination(gene, transportEnum, headerLength, tailLength);
						break;
					}
				}			
			}
		}
	}
	/**
	 * 普通基因的基因转座，即将指定基因插入个体的首位，成为第一个基因
	 * @param genes 个体中所有的基因
	 * @param sourceIndex 将要插入头部的基因
	 */
	private void transportParaDetermination(List<Gene> genes,int sourceIndex){
		Gene gene=genes.remove(sourceIndex);
		genes.add(0, gene);
	}
	/**
	 * 确定IS和RIS转座相关的参数，并通过调用有关方法开始转座
	 * @param gene 待转座基因
	 * @param transportEnum 转座类型枚举类
	 * @param headerLength 转座基因头长
	 * @param tailLength 转座基因尾长
	 */
	private void transportParaDetermination(Gene gene,TransportEnum transportEnum,int headerLength,int tailLength){
		Random sourceLocRandom=new Random();
		Random destLocRandom=new Random();
		Random elementLengthRandom=new Random();
		int elementLength=0;
		int sourceLoc=0;
		int destLoc=0;
		switch (transportEnum) {
		case IS:
			elementLength=transportEnum.transportElement[elementLengthRandom.nextInt(transportEnum.transportElement.length)];
			sourceLoc=sourceLocRandom.nextInt(headerLength+tailLength-elementLength);
			destLoc=destLocRandom.nextInt(headerLength-elementLength-1)+1;
			break;
		case RIS:
			elementLength=transportEnum.transportElement[elementLengthRandom.nextInt(transportEnum.transportElement.length)];
			sourceLoc=searchFunction(gene, destLocRandom.nextInt(headerLength));
			destLoc=0;
			break;
		}
		if(sourceLoc!=-1){
			transportBegin(gene.getGenePieces(), sourceLoc, destLoc, elementLength, headerLength, transportEnum);
		}
	}
	/**
	 * 在给定的基因中指定为位置后面寻找第一个函数，若找到则返回函数的index，若找不到则返回－1
	 * @param gene 待搜索的基因
	 * @param index 开始搜索的位置
	 * @return 结果
	 */
	private int searchFunction(Gene gene,int index){
		int result=-1;
		for(int i=index;i<gene.getGenePieces().size();i++){
			if(gene.getGenePieces().get(i).getGenePieceType()==GenePieceType.Function){
				result=i;
				break;
			}
		}
		return result;
	}
	/**
	 * 开始转座
	 * @param genePieces 待转座的基因片段
	 * @param source 转座开始的地方
	 * @param dest 转座的目标
	 * @param length 转座的长度
	 * @param headerLength 基因头长
	 * @param transportEnum 转座枚举类型
	 */
	private void transportBegin(List<GenePiece> genePieces,int source,int dest,int length,int headerLength,TransportEnum transportEnum){
		List<GenePiece> copiedSource=new ArrayList<GenePiece>(length);
		for(int i=0;i<length;i++)
			copiedSource.add((genePieces.get(i+source).clone()));
		int forLength=headerLength-dest-length;
		for(int i=0;i<forLength;i++){
			genePieces.set(headerLength-i-1, genePieces.get(headerLength-i-1-length));
		}
		for(int i=0;i<length;i++){
			genePieces.set(dest+i, copiedSource.get(i));
		}
	}
	/**
	 * 遍历种群中的每一个个体，进行重组，如果发生重组，每个个体只能在本函数调用中发生一次重组
	 * @param individualConfiguration 个体配置信息
	 * @param population 种群
	 * @param recombine 重组枚举类型
	 */
	private void iterateGeneInRecombine(IndividualConfiguration individualConfiguration,Population population,Recombine recombine){
		Random recombineOneRandom=new Random();
		Random recombineOtherRandom=new Random();
		for(int i=0;i<population.getIndividuals().size()-1;i++){
			if(recombineOneRandom.nextFloat()<recombine.getRate()){
				for(int j=i+1;j<population.getIndividuals().size();j++){
					if(recombineOtherRandom.nextFloat()<recombine.getRate()){
						recombineParaDetermination(population.getIndividuals().get(i), population.getIndividuals().get(j), recombine, individualConfiguration);
						break;
					}
				}
			}
		}
	}
	/**
	 * 确定重组的各种参数
	 * @param a 参加重组的个体a
	 * @param b 参加重组的个体b
	 * @param recombine 重组的枚举变量
	 * @param individualConfiguration 个体配置信息
	 */
	private void recombineParaDetermination(Individual a,Individual b,Recombine recombine,IndividualConfiguration individualConfiguration){
		Random startRandom=new Random();
		Random endRandom=new Random();
		GeneConfiguration geneConfiguration=individualConfiguration.getGeneConfiguration();
		int start=-1;
		int end=-1;
		switch (recombine) {
		case OnePoint:
			end=individualConfiguration.getNormalGeneTotalLength();
			start=startRandom.nextInt(end);
			break;
		case TwoPoint:
			start=startRandom.nextInt(individualConfiguration.getNormalGeneTotalLength());
			end=endRandom.nextInt(individualConfiguration.getNormalGeneTotalLength());
			if(start>end){
				int temp=start;
				start=end;
				end=temp;
			}
			if (start==end) {
				return;
			}
			break;
		case GENE:
			int geneNo=startRandom.nextInt(geneConfiguration.getNormalGeneNumber());
			start=geneNo*geneConfiguration.getNormalGeneLength();
			end=start+geneConfiguration.getNormalGeneLength();
			break;
		}
		beginRecombine(start, end, a, b,geneConfiguration);
	}
	/**
	 * 开始正式进行重组
	 * @param start 重组开始的位置，这个位置是在个体所有基因位里面的index
	 * @param end 重组结束的位置，这个位置是在个体所有基因位里面的index
	 * @param a 参加重组的个体a
	 * @param b 参加重组的个体b
	 * @param geneConfiguration 基因配置信息
	 */
	private void beginRecombine(int start,int end,Individual a,Individual b,GeneConfiguration geneConfiguration){
		Gene gene;
		GenePiece genePiece;
		List<GenePiece> aGenePieces;
		List<GenePiece> bGenePieces;
		int startGeneNo=start/geneConfiguration.getNormalGeneLength();
		int startGenePieceNo=start%geneConfiguration.getNormalGeneLength();
		int endGeneNo=end/geneConfiguration.getNormalGeneLength();
		int endGenePieceNo=end%geneConfiguration.getNormalGeneLength();
		if(endGenePieceNo==0){
			endGeneNo--;
			endGenePieceNo=geneConfiguration.getNormalGeneLength();
		}
		aGenePieces=a.getGenes().get(startGeneNo).getGenePieces();
		bGenePieces=b.getGenes().get(startGeneNo).getGenePieces();
		for(int i=startGenePieceNo;i<geneConfiguration.getNormalGeneLength();i++){
			genePiece=aGenePieces.get(i);
			aGenePieces.set(i , bGenePieces.get(i));
			bGenePieces.set(i, genePiece);
		}
		if(startGeneNo!=endGeneNo){
			for(int i=0;i<endGeneNo-startGeneNo-1;i++){
				gene=a.getGenes().get(i+startGeneNo+1);
				a.getGenes().set(i+startGeneNo+1, b.getGenes().get(i+startGeneNo+1));
				b.getGenes().set(i+startGeneNo+1, gene);
			}
			aGenePieces=a.getGenes().get(endGeneNo).getGenePieces();
			bGenePieces=b.getGenes().get(endGeneNo).getGenePieces();
			for(int i=0;i<endGenePieceNo;i++){
				genePiece=aGenePieces.get(i);
				aGenePieces.set(i, bGenePieces.get(i));
				bGenePieces.set(i, genePiece);
			}			
		}
	}
}
