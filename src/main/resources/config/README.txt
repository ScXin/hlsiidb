css/                                     页面样式代码
img/                                    默认图片显示背景
js/                                        javascript 代码
logo/                                   NSRL logo
META-INF/                          项目信息文件，用来配置应用程序、扩展程序、类加载器和服务
Properties/                           配置文件
          /archiveDataPath       archive数据源配置文件
          /config.properties      网站运行配置文件
          /HLS_operate_schdule_2015.conf     年度运行模式安排表，每年安排好运行模式后，需要把表放到这里
          /IntegralCurrentCacheData             已经计算的积分流强数据，随时更新可以减少查询积分流强时间
temp/                                  零时文件
WEB-INF/                         
       /lib/                              运行时需要的库
       /class/                           编译后的java文件
             /beans.xml              Spring bean配置页面（包括数据源配置）
             /struts.xml               页面跳转逻辑配置			 
data.jsp                                数据查询子页面
error.jsp                                错误页面
home.jsp                              查询首页面
index.jsp                               首页，显示运行状态
rawData.jsp                          原始数据查看页面
statistics.jsp	                        数据统计结果也难
status.jsp                              束流寿命及状态页面
statusList.jsp                         运行状态列表
statusStatistics.jsp                 运行状态统计画图
userInputPv.jsp                     用户自由输入pv名画图
integralCurrent.jsp                积分流强查询画图页面
integralCurrentList.jsp           积分流强数据显示页面
