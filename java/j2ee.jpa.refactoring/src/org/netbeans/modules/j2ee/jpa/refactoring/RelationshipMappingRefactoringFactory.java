/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.j2ee.jpa.refactoring;

import org.netbeans.api.fileinfo.NonRecursiveFolder;
import org.netbeans.api.java.source.TreePathHandle;
import org.netbeans.modules.j2ee.jpa.refactoring.rename.RelationshipMappingRename;
import org.netbeans.modules.j2ee.jpa.refactoring.whereused.RelationshipMappingWhereUsed;
import org.netbeans.modules.refactoring.api.AbstractRefactoring;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.modules.refactoring.api.WhereUsedQuery;
import org.netbeans.modules.refactoring.spi.RefactoringPlugin;
import org.netbeans.modules.refactoring.spi.RefactoringPluginFactory;
import org.openide.filesystems.FileObject;

/**
 *
 * @author ralph
 */
@org.openide.util.lookup.ServiceProvider(service = RefactoringPluginFactory.class)
public class RelationshipMappingRefactoringFactory implements RefactoringPluginFactory {

    public RelationshipMappingRefactoringFactory() {
    }

    @Override
    public RefactoringPlugin createInstance(AbstractRefactoring refactoring) {

        FileObject targetFile = refactoring.getRefactoringSource().lookup(FileObject.class);
        NonRecursiveFolder pkg = refactoring.getRefactoringSource().lookup(NonRecursiveFolder.class);
        TreePathHandle handle = refactoring.getRefactoringSource().lookup(TreePathHandle.class);

        boolean folder = targetFile != null && targetFile.isFolder();
        boolean javaPackage = pkg != null && RefactoringUtil.isOnSourceClasspath(pkg.getFolder());
        boolean javaFile = targetFile != null && RefactoringUtil.isJavaFile(targetFile);
        boolean javaMember = handle != null;

        if (refactoring instanceof RenameRefactoring) {
            RenameRefactoring rename = (RenameRefactoring) refactoring;
            if (!javaFile && !(javaPackage || folder) && javaMember) {
                return new RelationshipMappingRename(rename);
            }
        } else if (refactoring instanceof WhereUsedQuery) {
            WhereUsedQuery whereUsedQuery = (WhereUsedQuery) refactoring;
            return new RelationshipMappingWhereUsed(whereUsedQuery);
        }
        return null;
    }
}
