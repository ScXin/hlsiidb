package com.example.hlsiidb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import com.example.hlsiidb.dto.PvInfo;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * read configuration
 * @author sigema
 */
public class ReadConfig {

	//存储所有pv名和单位的map Map<pv值，[unit,filename]>
	private static Map<String,String[]> pvMap;

	/**
	 * 静态代码块，根据配置文件初始化pvMap
	 */
	static {
	    pvMap = new HashMap<>();
        String fileDirPath = Config.DEFAULT_PATH + "/config";
        File file = new File(fileDirPath);
        String[] fileList = file.list();
        for (int i = 0; i < fileList.length; i++) {
            if(fileList[i].split("\\.").length <= 1 || !fileList[i].split("\\.")[1].equals("xml")
                    || fileList[i].split("\\.")[0].equals("BeamCurrent")){
                continue;
            }
            SAXBuilder builder = new SAXBuilder();
            Document doc = null;
            try {
                doc = builder.build(fileDirPath + "/" + fileList[i]);
            } catch (JDOMException e) {
                e.printStackTrace();
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Element groups = doc.getRootElement();
            if(groups.getName().equals("groups")) {
                List<Element> topList = groups.getChildren("group");
                if(topList == null || topList.size() == 0){
                    continue;
                }
                List<Element> channel = topList.get(0).getChildren("channel");
                List<Element> unit = topList.get(1).getChildren("unit");
                if(unit == null || unit.size() == 0){
                    continue;
                }
                for (Element element :
                        channel) {
                    pvMap.put(element.getAttributeValue("name"), new String[]{unit.get(0).getValue(),fileList[i]});
                }
            }
            pvMap.put("RNG:BEAM:CURR",new String[]{"Current(mA)","BeamCurrnt1"});
            pvMap.put("RNG:BEAM:LIFE",new String[]{"Lifetime(hrs)","BeamCurrnt2"});
        }
    }
	/**
	 * read ioc_config.xml config
	 * return topMap like this: 
	 * Linac {GUN=[], PS=[QM, CM, LC-SC-AM],}     Trans {PS=[QM, CM, IC-BM]    SR {PS=[BM-SM-SKM, QM
	 * @param fileDir
	 * @return topMap
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map<String, Map<String, List<String>>> readChannelRelation(String fileDir) throws JDOMException, IOException{
		//创建一个SAXBuilder对象
		SAXBuilder saxBuilder = new SAXBuilder();
		//读取prop.xml资源
		Document doc = saxBuilder.build(fileDir);
		//获取根元素(root)
		Element root = doc.getRootElement();
		//获取根元素下面的所有子元素(top_group)
		List<Element> topList = root.getChildren("top_group");
		// 最后返回的容器对象
		Map<String, Map<String, List<String>>> topMap = new LinkedHashMap<String, Map<String, List<String>>>();
		//遍历根元素的子元素集合(即遍历mess元素)
		for(int i = 0; i < topList.size(); i++){
			List<Element> midList = topList.get(i).getChildren("mid_group");
			Map<String, List<String>> midMap = new LinkedHashMap<String, List<String>>();
			for(int j = 0; j < midList.size(); j++){
				List<Element> subList = midList.get(j).getChildren("sub_group");
				List<String> sub = new ArrayList<String>();
				for(int k = 0;k<subList.size();k++){
					sub.add(subList.get(k).getAttributeValue("value"));
				}
				midMap.put(midList.get(j).getAttributeValue("value"), sub);
			}
			topMap.put(topList.get(i).getAttributeValue("value"), midMap);
		}
		//遍历完没有查找到结果返回null
		return topMap;
	}
	
	/**
	 * To get value by one key in file , if file doesn't exist,reutrn null
	 * @param fileDir
	 * @param key
	 * @return String
	 */
	public static String getValue(String fileDir, String key) {
		File file = new File(fileDir);
		if (!file.exists())
			return null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties p = new Properties();
		try {
			p.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return p.getProperty(key);
	}

	/**
	 * To get value by several keys in file, if file doesn't exist,reutrn null
	 * @param fileDir
	 * @param keys
	 * @return String[]
	 */
	public static String[] getValue(String fileDir,String[] keys) {
		File file = new File(fileDir);
		if (!file.exists())
			return null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties p = new Properties();
		try {
			p.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String result[] = new String[keys.length];
		int count = 0;
		for (String key : keys) {
			result[count++] = p.getProperty(key);
		}
		return result;
	}

	/**
	 * 根据设备名从配置文件中找出相应的pv list
	 * @param name 格式为    Name1_Name2_Name3
	 * @return 返回PvInfo对象的集合
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static List<PvInfo> getEquipList(String name) throws IOException, JDOMException {
		File file = new File(Config.DEFAULT_PATH + "/config/" + name + ".xml");
		List<PvInfo> list = new ArrayList<>();
		if(file != null) {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(file);
			Element groups = doc.getRootElement();
			List<Element> topList = groups.getChildren("group");
			List<Element> channel = topList.get(0).getChildren("channel");
			Map<String,String> map = new HashMap<>();
			for (Element element:
				 channel) {
				map.put(element.getValue(),element.getAttributeValue("name"));
				list.add(new PvInfo(element.getText(),element.getAttributeValue("name")));
			}
		}
		return list;
	}

	/**
	 * 根据xml配置文件获取需要辐射监控的PV列表
	 * @return pv名的集合
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static List<String> getDmList() throws IOException, JDOMException{
		File file = new File(Config.DEFAULT_PATH + "/config/Radiation.xml");
		List<String> channelNames = new ArrayList<>();
		if(file != null) {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(file);
			Element groups = doc.getRootElement();
			List<Element> topList = groups.getChildren("group1");
			List<Element> subList = topList.get(0).getChildren("channel");
			for (int i = 0; i < subList.size(); i++) {
				channelNames.add(subList.get(i).getValue().split("-")[0]);
			}
		}else{
			return null;
		}
		return channelNames;
	}

	/**
	 * 检测pv是否存在于xml配置文件中
	 * @param pvName
	 * @return
	 */
	public static boolean checkPvExistence(String pvName){
        if(!pvMap.containsKey(pvName)){
            return false;
        }
	    return true;
	}


	public static Map<String,List<String>> getMorePvInfo(String[] pvList){
	    Map<String,List<String>> map = new HashMap<>();
	    Map<String,String> mapTemp = new HashMap<>();
        for (int i = 0; i < pvList.length; i++) {
            if(!checkPvExistence(pvList[i])){
                return null;
            }
            String unit = pvMap.get(pvList[i])[0];
            String fileName = pvMap.get(pvList[i])[1];
            String newKey = fileName+"@"+unit;
            if(mapTemp.containsKey(newKey)){
                map.get(newKey).add(pvList[i]);
            }else{
                map.put(newKey,new ArrayList<>());
                map.get(newKey).add(pvList[i]);
            }
        }
        return map;
    }

	/**
	 * 根据pv名从配置文件中查询对应的单位
	 * @param pv
	 * @return
	 */
	public static String getPvUnit(String pv){
        System.out.println(pv);
	    if(pvMap.containsKey(pv)) {
            return pvMap.get(pv)[0];
        }else {
	        return "unknown";
        }
    }

}