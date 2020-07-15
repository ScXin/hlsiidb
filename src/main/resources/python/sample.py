#!/usr/bin/python
from epics import PV
import time
import cPickle as p

samplefile1 = './sample1.txt'
samplefile1_backup = './sample1_backup.txt'
samplefile2 = './sample2.txt'
samplefile2_backup = './sample2_backup.txt'
samplefile3 = './parameters.properties'

f1 = file(samplefile1_backup)
if len(open(samplefile1_backup).read())>0:
  sampletime_list1=p.load(f1)
else:
  sampletime_list1=[]
f2 = file(samplefile2_backup)
if len(open(samplefile2_backup).read())>0:
  sampletime_list2=p.load(f2)
else:
  sampletime_list2=[]

pv1 = PV('cigema:calcBeamCurr:calc')
pv2 = PV('cigema:calcBeamLife:calc')
pv1Disconnect = 0
pv2Disconnect = 0

while True:
  f3 = file(samplefile3,'w')
  f3.write("operationmode=user operation\n")
  f3.write("operationstatus=light available\n")
  f3.write("energy=800MeV\n")
  f3.write("integratedcurrent=0AH\n")
  f3.write("beamcurrent=%.2f mA\n" %(pv1.value))
  f3.write("lifetime=%.2f hours" %(pv2.value))
  f3.close()

  if str(pv1.status) == "None":
     #print("pv1Disconnect=%d,  pv1.status is None" %(pv1Disconnect))
     if pv1Disconnect == 3:
        #print("send eamil when pv1Disconnect == 3")
        os.system("echo 'pv1 is disconnected' |mail -s 'Warning from Database_App2' gfliu@ustc.edu.cn")
     pv1Disconnect = pv1Disconnect + 1
     time.sleep(29)
     continue
  else:
     pv1Disconnect = 0

  if str(pv2.status) == "None":
     #print("pv2Disconnect=%d,  pv2.status is None" %(pv2Disconnect))
     if pv2Disconnect == 3:
        #print("send eamil when pv2Disconnect == 3")
        os.system("echo 'pv2 is disconnected' |mail -s 'Warning from Database_App2' gfliu@ustc.edu.cn")
     pv2Disconnect = pv2Disconnect + 1
     time.sleep(29)
     continue
  else:
     pv2Disconnect = 0

  if len(sampletime_list1)==3000:
     del sampletime_list1[0]
     sampletime_list1.append(str(pv1.value)+" "+str(pv1.timestamp))
     f1 = file(samplefile1,'w')
     f1.write(str(sampletime_list1))
     f1_backup = file(samplefile1_backup,'w')
     p.dump(sampletime_list1,f1_backup)
     f1.close()
     f1_backup.close()
  else:
     sampletime_list1.append(str(pv1.value)+" "+str(pv1.timestamp))
     f1 = file(samplefile1,'w')
     f1.write(str(sampletime_list1))
     f1_backup = file(samplefile1_backup,'w')
     p.dump(sampletime_list1,f1_backup)
     f1.close()
     f1_backup.close()
  #print("pv1"+str(len(sampletime_list1)))

  if len(sampletime_list2)==3000:
     del sampletime_list2[0]
     sampletime_list2.append(str(pv2.value)+" "+str(pv2.timestamp))
     f2 = file(samplefile2,'w')
     f2.write(str(sampletime_list2))
     f2_backup = file(samplefile2_backup,'w')
     p.dump(sampletime_list2,f2_backup)
     f2.close()
     f2_backup.close()
  else:
     sampletime_list2.append(str(pv2.value)+" "+str(pv2.timestamp))
     f2 = file(samplefile2,'w')
     f2.write(str(sampletime_list2))
     f2_backup = file(samplefile2_backup,'w')
     p.dump(sampletime_list2,f2_backup)
     f2.close()
     f2_backup.close()
  #print("pv2"+str(len(sampletime_list2)))

  time.sleep(29)

