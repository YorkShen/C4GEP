package domain.core.algOutput;

import java.io.Serializable;

import domain.core.algconfiguration.Function;

/**
 * ����Ƭ���࣬��ƻ���λ�࣬��ÿһ��ͷ������β���У����հ����Ķ��Ǵ���GenePiece
 * @author ��Զ
 *
 */
public class GenePiece implements Serializable,Cloneable{
	private static final long serialVersionUID = 7648117930489664221L;
	private String symbol;
	private String name;
	private Float value;
	private GenePieceType genePieceType;
	private Function function;
	/**
	 * ���ص�ǰ����Ƭ�εķ��ű�ʾ����String��ʾ
	 * @return ����Ƭ�η��ű�ʾ
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * ���õ�ǰ����Ƭ�εķ��ű�ʾ����String��ʾ
	 * @param symbol ����Ƭ�η��ű�ʾ
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	/**
	 * ���ص�ǰ����Ƭ�ε�����
	 * @return ����Ƭ�����Ƶ��ַ�����ʾ
	 */
	public String getName(){
		return name;
	}
	/**
	 * ���õ�ǰ����Ƭ�ε�����
	 * @param name ��ǰ����Ƭ�ε�����
	 */
	public void setName(String name){
		this.name=name;
	}
	/**
	 * ��õ�ǰ����Ƭ�ε�ֵ
	 * @return ��ǰ����Ƭ�ε�ֵ
	 */
	public Float getValue(){
		return value;
	}
	/**
	 * ���õ�ǰ����Ƭ�ε�ֵ
	 * @param value ����Ƭ�ε�ֵ
	 */
	public void setValue(Float value){
		this.value=value;
	}
	public GenePieceType getGenePieceType() {
		return genePieceType;
	}
	public void setGenePieceType(GenePieceType genePieceType) {
		this.genePieceType = genePieceType;
	}
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	/**
	 * ���ص�ǰ����Ƭ�ε����Ʊ�ʾ����������ֱ�ӵ���getName()��
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
	/**
	 * ��¡һ�ݵ�ǰ�����������������Object�е�clone������
	 * ����һ����ȿ�¡����Դ������¶��������ͬ�ĳ�ֵ�������в�ͬ���ڴ��ַ��
	 */
	@Override
	public GenePiece clone(){
		// TODO Auto-generated method stub
		GenePiece o=null;
		try {
			o=(GenePiece) super.clone();
			if(function!=null)
				o.function=function.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
}