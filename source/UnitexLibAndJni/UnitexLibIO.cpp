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
 * https://github.com/ergonotics/JNI-for-Unitex-2.1
 * contact : unitex-contribution@ergonotics.com
 *
 */


#include <string.h>
#include <stdlib.h>

#include "Af_stdio.h"
#include "DirHelper.h"
#include "UnitexLibDir.h"
#include "UnitexLibIO.h"


/**
 * Get a const void* pointer to a raw Unitex file content (from system filesystem or filespace)
 * *buffer will receive the pointer with file content
 * *size_file will receive the file size
 * *umf will receive a handle needed for releasing
 */
UNITEX_FUNC void UNITEX_CALL GetUnitexFileReadBuffer(const char*name,UNITEXFILEMAPPED** umf, const void**buffer,size_t *size_file)
{
    *buffer = NULL;
    *size_file = 0;

    ABSTRACTMAPFILE*amf = af_open_mapfile(name,MAPFILE_OPTION_READ,0);
    if (amf != NULL)
    {
        *size_file = af_get_mapfile_size(amf);
        *buffer = af_get_mapfile_pointer(amf, 0, *size_file);
    }
    *umf=(UNITEXFILEMAPPED*)amf;
}


/**
  * Release buffer returned by GetUnitexFileReadBuffer
  */
UNITEX_FUNC void UNITEX_CALL CloseUnitexFileReadBuffer(UNITEXFILEMAPPED *umf,const void*buffer,size_t size_file)
{
    ABSTRACTMAPFILE*amf=(ABSTRACTMAPFILE*)umf;
    if (amf != NULL)
    {
        af_release_mapfile_pointer(amf,buffer,size_file);
        af_close_mapfile(amf);
    }
}


/**
 * Write an Unitex file content (to system filesystem or filespace)
 * it write from two buffer (prefix and suffix). This is useful for writing both header and footer (or BOM and text...)
 */
UNITEX_FUNC int UNITEX_CALL WriteUnitexFile(const char*name,const void*buffer_prefix,size_t size_prefix,const void*buffer_suffix,size_t size_suffix)
{
    ABSTRACTFILE* vfWrite = af_fopen(name, "wb");
    if (vfWrite == NULL)
    {
        return 1;
    }
    int retValue = 0;
    if (size_prefix > 0)
        if (size_prefix != af_fwrite(buffer_prefix,1,size_prefix,vfWrite))
            retValue = 1;

    if (retValue==0 && (size_suffix > 0))
        if (size_suffix != af_fwrite(buffer_suffix,1,size_suffix,vfWrite))
            retValue = 1;

    af_fclose(vfWrite);
    return retValue;
}


/**
 * remove a file
 */
UNITEX_FUNC int UNITEX_CALL RemoveUnitexFile(const char*name)
{
    return af_remove(name);
}


/**
 * rename a file
 */
UNITEX_FUNC int UNITEX_CALL RenameUnitexFile(const char*oldName,const char*newName)
{
    return af_rename(oldName,newName);
}


/**
 * copy a file
 */
UNITEX_FUNC int UNITEX_CALL CopyUnitexFile(const char*srcName,const char*dstName)
{
    return af_copy(srcName,dstName);
}


int af_create_folder_unlogged(const char*folderName)
{
    //if (is_filename_in_abstract_file_space(folderName)==0)
        return mkDirPortable(folderName);
    /*
    else
    {
        int retValue =  0;
        return retValue;
    }
     */
}


/**
 * create a folder, if needed
 */
UNITEX_FUNC int UNITEX_CALL CreateUnitexFolder(const char*folderName)
{
    return af_create_folder_unlogged(folderName);
}

int af_remove_folder_unlogged(const char*folderName)
{
    if (is_filename_in_abstract_file_space(folderName)==0)
            return RemoveFileSystemFolder(folderName);
    else
    {
        char*folderNameStar=(char*)malloc(strlen(folderName)+4);
        if (folderNameStar == NULL)
            return -1;
        strcpy(folderNameStar,folderName);
        strcat(folderNameStar,"*");
        int retValue =  af_remove_unlogged(folderNameStar);
        free(folderNameStar);
        return retValue;
    }
}

/**
 * remove a folder and the folder content
 */
UNITEX_FUNC int UNITEX_CALL RemoveUnitexFolder(const char*folderName)
{
    return af_remove_folder_unlogged(folderName);
}
