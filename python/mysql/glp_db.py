
#  This is a class to access the glp mysql data base.  The 
#  purpose of this is to learn a bit more python in a somewhat
#  realistic problem.


#  Most of the web instructions for using mysql (at least those
#  which I looked at) say to import PyMySQL.  The PyMySQL does
#  not work on my linux box (fedora 26).  I found a suggestion
#  to use all lower case, anf that works for me.

import pymysql

# regular expression stuff and timing stuff
import re
import time


#  The glp_fileio module has the GLPFileIO class, which reads
#  data from the random access points files associated with the
#  glp database.

import glp_fileio


# class to hold id values (object, file, file pos) that are
# returned by a "spatial" query of the database

class ObjFilePos:
  def __init__ (self, obid, f_id, f_pos):
    self.object_id = obid
    self.file_id = f_id
    self.file_pos = f_pos


#  Encapsulate the glp database stuff in class GLPDB

class GLPDB:

#  The constructor needs to establish connection to the mysql dbase.

  def __init__ (self):

    self.dirname = ""
    self.connection = pymysql.connect (host = "localhost",\
                                       user = "glenn",\
                                       password = "Dog4-J3ssi3",\
                                       db = "glp")
    try:
      with self.connection.cursor() as curs:
        sq = "select * from global_stuff"
        curs.execute(sq)
        for (dirname, xmin, ymin, xmax, ymax, ixmin, iymin, ixmax, iymax) in curs:
          self.fio = glp_fileio.GLPFileIO (dirname,\
                                           xmin, ymin,\
                                           xmax, ymax,\
                                           ixmin, iymin,\
                                           ixmax, iymax)
          self.dirname = dirname
          break

    finally:
      pass
                                       

# !! end of __init__ constructor


# Execute the bbox select query

  def __do_bbox_query_ (self, sq):

    rlst = []

    with self.connection.cursor() as curs:
      curs.execute(sq)
      for (object_id, line_id, file_id, file_pos) in curs:
        ofp = ObjFilePos (int(object_id), int(file_id), int(file_pos))
        rlst.append(ofp)

      return rlst          
    
    


#  Method for trivial file io test.

  def test_fileio (self):

    with open ("/home/gpinkerton/data/glp_points_1.dat", "r+b") as tf:
      plist = self.fio.read_points (tf, 0)
      print ("")
      for upt in plist:
        print ("  x = " + str(upt.x) + "  y = " + str(upt.y))

# !! end of test_fileio method


#  Method to disconnect from the mysql server

  def disconnect (self):
    self.connection.close()

# !! end of disconnect method


# create an sql string to select objects intersecting
# the specified rectangle

  def __create_bbox_query_ (self, bxmin, bymin, bxmax, bymax):

    ix1 = int(bxmin * 1000000.0)
    iy1 = int(bymin * 1000000.0)
    ix2 = int(bxmax * 1000000.0)
    iy2 = int(bymax * 1000000.0)

    bbs =\
      "'polygon ((\n" +\
         str(ix1) + " " + str(iy1) + " , \n" +\
         str(ix1) + " " + str(iy2) + " , \n" +\
         str(ix2) + " " + str(iy2) + " , \n" +\
         str(ix2) + " " + str(iy1) + " , \n" +\
         str(ix1) + " " + str(iy1) + " \n" + "))'"

    s = "select objects.object_id,pline_points_lookup.* from \n" +\
          "objects_geom,objects,pline_id_lookup,pline_points_lookup \n" +\
          " where \n" +\
          "(MBRIntersects" + "\n" +\
             "(ST_GeomFromText\n(" +\
               bbs + "),\n objects_geom.bbox)) \n" +\
          "  and \n" +\
          "(objects.object_id = objects_geom.object_id) \n" +\
          "  and \n" +\
          "(pline_id_lookup.object_id = objects.object_id) \n" +\
          "  and \n" +\
          "(pline_points_lookup.line_id = pline_id_lookup.line_id)\n" +\
          "order by pline_points_lookup.file_id, pline_points_lookup.file_pos"

    return s

# !! end of __create_bbox_query_ method



#  function to prompt for bbox limits, get the obkects intersecting
#  the bbox and wite them to a file.

  def test_bbox (self):

    try:
      fpts = open (self.dirname + "/glp_points_1.dat", "rb")
    except IOError:
      print ("****** Cannot open file: glp_points.dat ******")
      return

    try:
      fout = open ("py_bbox_sel.out", "w")
    except IOError:
      print ("****** Cannot open file: py_bbox_sel.out ******")
      return

    BBERR = "Illegal bbox definition, try again"
    while True:
      try:
        print ("")
        c = input("Enter lower left x y and upper right x y: ")
        if len(c) < 1:
          break

        try:
          bblst = re.split(" ", c)
        except Exception as e:
          print ("Exception from re.split: " + str(e))
          continue

        tstart = time.time()

        sq = self.__create_bbox_query_ (float(bblst[0]), float(bblst[1]),
                                        float(bblst[2]), float(bblst[3]))
        file_pos_list = self.__do_bbox_query_ (sq)
        sl = str(len(file_pos_list))

        fout.write ("\n==== new search bbox =  " + c + "  ====\n")
        fout.write ("     number of rows found = " + sl + "\n")
        print ("\n     number of rows found = " + sl + "\n")

        for ofp in file_pos_list:
      #    print ("")
      #    print ("object id = " + str(ofp.object_id))
      #    print ("file id = " + str(ofp.file_id))
      #    print ("file pos = " + str(ofp.file_pos))
          fout.write ("\n")
          fout.write ("object_id = " + str(ofp.object_id))
          fout.write ("\n")
          fout.write ("file_id = " + str(ofp.file_id))
          fout.write ("\n")
          fout.write ("file_pos = " + str(ofp.file_pos))
          fout.write ("\n")
          plist = self.fio.read_points (fpts, ofp.file_pos)
          for upt in plist:
            fout.write(f"  x = {upt.x:.5f}  y = {upt.y:.5f}")
#            fout.write("  x = " + str(round(upt.x, 5)) +\
#                       "  y = " + str(round(upt.y, 5)))
            fout.write ("\n")


      except Exception as e:
        print ("Exception in test_bbox: " + str(e))
    # close output file and point before raising the exception
        if not fout.closed:
          fout.close()
        if not fpts.closed:
          fout.close()
        raise

      tend = time.time() 
      delta_time = round(tend - tstart, 2)

      print ("")
      print ("elapsed time for query = " + str(delta_time))
      print ("")

  #  ! end of while true loop to prompt for bbox limits and process them


  # comes here if no exception happened 
  # still probably need to close the output and point files
    if not fout.closed:
      fout.close()
    if not fpts.closed:
      fout.close()

    return
      

# !! end of test_bbox method



## !!! end of GLPDB class definition




# make a class and print the first "line" points in file
# This code is not part of the GLPDB class

db = GLPDB ()
db.test_bbox()



