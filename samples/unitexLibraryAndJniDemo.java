/*
 * Unitex
 *
 * Copyright (C) 2001-2011 Universit� Paris-Est Marne-la-Vall�e <unitex@univ-mlv.fr>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 *
 */

/*
 * File created and contributed by Gilles Vollant (Ergonotics SAS) 
 * as part of an UNITEX optimization and reliability effort
 *
 * additional information: http://www.ergonotics.com/unitex-contribution/
 * contact : unitex-contribution@ergonotics.com
 *
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.unitex.jni.UnitexJni;

public class unitexLibraryAndJniDemo {

      private static final String pathSeparator = UnitexJni.isUnderWindows() ? "\\" : "/";

      private static String processUnitexWork(String othersResDir,String dictionnaryResDir,String graphResDir,String corpusPath,String corpusText)
      {

          String pSep = pathSeparator;
          UnitexJni.writeUnitexFile(UnitexJni.combineUnitexFileComponent(corpusPath,"corpus.txt"),corpusText);

          String cmdNorm = "Normalize " + UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.txt") + " -r "+UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Norm.txt") ;
          String cmdTok = "Tokenize " + UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.txt") + " -a "+ UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Alphabet.txt") ;
          String cmdDico = "Dico -t "+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.snt")+ " -a " + UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Alphabet.txt")+" "+UnitexJni.combineUnitexFileComponentWithQuote(dictionnaryResDir,"dela-en-public.bin") ;
          String cmdLocate = "Locate -t "+UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.snt")+ " " + UnitexJni.combineUnitexFileComponentWithQuote(graphResDir,"AAA-hoursgilles.fst2")+ " -a " + UnitexJni.combineUnitexFileComponentWithQuote(othersResDir,"Alphabet.txt")+ " -L -R --all -b -Y";
          String cmdConcord = "Concord "+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus_snt","concord.ind")+ " -m " + UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus.txt") ;
          
          String cmdConcord2 = "Concord "+ UnitexJni.combineUnitexFileComponentWithQuote(corpusPath,"corpus_snt","concord.ind")+" --xml";

          /*
           *
           // there is an alternative : using an array of string
          String [] strArrayNormalize={"UnitexTool","{","Normalize",UnitexJni.combineUnitexFileComponent(corpusPath,"corpus.txt"), "-r",UnitexJni.combineUnitexFileComponent(othersResDir,"Norm.txt"),"}"};
          //String [] strArrayNormalizeAlternative={"UnitexTool","Normalize",UnitexJni.combineUnitexFileComponent(corpusPath,"corpus.txt"), "-r",UnitexJni.combineUnitexFileComponent(othersResDir,"Norm.txt")};
          UnitexJni.execUnitexTool(strArrayNormalize);
          */
          UnitexJni.execUnitexTool("UnitexTool " + cmdNorm);
          UnitexJni.execUnitexTool("UnitexTool " + cmdTok);
          UnitexJni.execUnitexTool("UnitexTool " + cmdDico);
          UnitexJni.execUnitexTool("UnitexTool " + cmdLocate);
          UnitexJni.execUnitexTool("UnitexTool " + cmdConcord);
          UnitexJni.execUnitexTool("UnitexTool " + cmdConcord2);
          
		  // these 6 lines can be replaced by only one execution (with very small speed improvement)			
          /*
          UnitexJni.execUnitexTool("UnitexTool { " + cmdNorm + " } { " + cmdTok + " } { " + cmdDico + " } { "  + cmdLocate + " } { " + cmdConcord + " } { " + cmdConcord2+ " }");
          
          */

          String merged =  UnitexJni.getUnitexFileString(UnitexJni.combineUnitexFileComponent(corpusPath,"corpus.txt"));
          String xml = UnitexJni.getUnitexFileString(UnitexJni.combineUnitexFileComponent(corpusPath,"corpus_snt","concord.xml"));
          return xml;
      }

    public static void main(String [] args) {

        System.out.println("is ms-windows:"+UnitexJni.isUnderWindows()+" : "+System.getProperty("os.name")+ " "+java.io.File.separator);


        String baseWorkDir = ".";
        String ressourceDir = UnitexJni.isUnderWindows() ? "y:\\avir\\demojnires": "/Users/gillesvollant/demojnires";
        int nbLoop=1;


        if (args.length>=1)
            ressourceDir=args[0];


        if (args.length>=2)
            baseWorkDir=args[1];

        if (args.length>=3)
            nbLoop=Integer.parseInt(args[2]);

        System.out.println("resource path : '"+ressourceDir+"' and work path is '"+baseWorkDir+"' and "+nbLoop+" execution");

        String graphResDir = UnitexJni.combineUnitexFileComponent(ressourceDir, "graph");
        String dictionnaryResDir = UnitexJni.combineUnitexFileComponent(ressourceDir, "dictionnary");
        String othersResDir = UnitexJni.combineUnitexFileComponent(ressourceDir, "others");



        UnitexJni.setStdOutTrashMode(true);
        //UnitexJni.setStdErrTrashMode(true);


        String CorpusWorkPath = UnitexJni.combineUnitexFileComponent(baseWorkDir, "workUnitexThread" + Thread.currentThread().getId());

        System.out.println("will work on "+CorpusWorkPath);
        UnitexJni.createUnitexFolder(CorpusWorkPath);
        UnitexJni.createUnitexFolder(UnitexJni.combineUnitexFileComponent(CorpusWorkPath,"corpus_snt"));
        String res="";

        long startT = System.currentTimeMillis();
        for (int i=0;i<nbLoop;i++)
        {
            res=processUnitexWork(othersResDir,dictionnaryResDir,graphResDir,CorpusWorkPath,
"I want watch at 4:00 am see at 6:00 pm before leave at 15.47");
        }
        long endT = System.currentTimeMillis();

        UnitexJni.removeUnitexFolder(CorpusWorkPath);

        System.out.println("");
        System.out.println("result:");
        System.out.println(res);
        System.out.println("time : "+(endT-startT)+" ms");
}

}
