package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Generator {
    public static void main(String[] args) throws Exception {

        int version=1;
        String defaultPackage="test.greenDAO.bean";
        //����ģʽ����ָ���汾�ź��Զ����ɵ�bean����İ���
        Schema schema=new Schema(version,defaultPackage);
        //ָ���Զ����ɵ�dao����İ���,��ָ����DAO��������"test.greenDAO.bean"����
        schema.setDefaultJavaPackageDao("test.greenDAO.dao");

        //���ʵ��
        addEntity(schema);

        String outDir=System.getProperty("user.dir")+"\\app\\src\\main\\java-gen";
        //����DaoGenerator().generateAll�����Զ����ɴ��뵽֮ǰ������java-genĿ¼��
        new DaoGenerator().generateAll(schema,outDir);

    }

    private static void addEntity(Schema schema) {
        //���һ��ʵ�壬����Զ�����ʵ��Entity��
        Entity entity = schema.addEntity("Entity");
        //ָ���������粻ָ����������Ϊ Entity����ʵ��������
        entity.setTableName("student");
        //��ʵ������������ԣ�����test��������ֶΣ�
        entity.addIdProperty().autoincrement();//���Id,������
        entity.addStringProperty("name").notNull();//���String���͵�name,����Ϊ��
        entity.addIntProperty("age");//���Int���͵�age
        entity.addDoubleProperty("score");//���Double��score
        entity.addDoubleProperty("test");//���Double��score
    }
}