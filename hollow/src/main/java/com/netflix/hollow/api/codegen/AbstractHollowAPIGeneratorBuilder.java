/*
 *  Copyright 2017 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.netflix.hollow.api.codegen;

import com.netflix.hollow.core.HollowDataset;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractHollowAPIGeneratorBuilder<B extends AbstractHollowAPIGeneratorBuilder<?, ?>, G extends HollowAPIGenerator> {
    protected String apiClassname;
    protected String packageName;
    protected HollowDataset dataset;
    protected Set<String> parameterizedTypes = Collections.emptySet();
    protected boolean parameterizeAllClassnames = false;
    protected boolean useErgonomicShortcuts = false;

    protected CodeGeneratorConfig config = new CodeGeneratorConfig();

    protected abstract G instantiateGenerator();

    protected abstract B getBuilder();

    public B withAPIClassname(String apiClassname) {
        this.apiClassname = apiClassname;
        return getBuilder();
    }

    public B withPackageName(String packageName) {
        this.packageName = packageName;
        return getBuilder();
    }

    public B withDataModel(HollowDataset dataset) {
        this.dataset = dataset;
        return getBuilder();
    }

    public B withParameterizedTypes(Set<String> parameterizedTypes) {
        this.parameterizedTypes = parameterizedTypes;
        return getBuilder();
    }

    public B withParameterizeAllClassNames(boolean parameterizeAllClassnames) {
        this.parameterizeAllClassnames = parameterizeAllClassnames;
        return getBuilder();
    }

    public B withClassPostfix(String classPostfix) {
        config.setClassPostfix(classPostfix);
        return getBuilder();
    }

    public B withGetterPrefix(String getterPrefix) {
        config.setGetterPrefix(getterPrefix);
        return getBuilder();
    }

    public B withAggressiveSubstitutions(boolean useAggressiveSubstitutions) {
        config.setUseAggressiveSubstitutions(useAggressiveSubstitutions);
        return getBuilder();
    }

    public B withErgonomicShortcuts() {
        this.useErgonomicShortcuts = true;
        return getBuilder();
    }

    public B withPackageGrouping() {
        config.setUsePackageGrouping(true);
        return getBuilder();
    }

    public B withBooleanFieldErgonomics(boolean useBooleanFieldErgonomics) {
        config.setUseBooleanFieldErgonomics(useBooleanFieldErgonomics);
        return getBuilder();
    }

    public B reservePrimaryKeyIndexForTypeWithPrimaryKey(boolean reservePrimaryKeyIndexForTypeWithPrimaryKey) {
        config.setReservePrimaryKeyIndexForTypeWithPrimaryKey(reservePrimaryKeyIndexForTypeWithPrimaryKey);
        return getBuilder();
    }

    /**
     * NOTE: Have to be enabled with withErgonomicShortcuts
     */
    public B withRestrictApiToFieldType() {
        config.setRestrictApiToFieldType(true);
        return getBuilder();
    }


    public B withHollowPrimitiveTypes(boolean useHollowPrimitiveTypes) {
        config.setUseHollowPrimitiveTypes(useHollowPrimitiveTypes);
        return getBuilder();
    }

    public G build() {
        if (apiClassname == null)
            throw new IllegalStateException("Please specify an API classname (.withAPIClassname()) before calling .build()");
        if (packageName == null)
            throw new IllegalStateException("Please specify a package name (.withPackageName()) before calling .build()");
        if (dataset == null)
            throw new IllegalStateException("Please specify a data model (.withDataModel()) before calling .build()");

        if(config.isRestrictApiToFieldType() && !useErgonomicShortcuts) {
            throw new IllegalStateException(" restrictApiToFieldType requires withErgonomicShortcuts");
        }

        G generator = instantiateGenerator();
        generator.setCodeGeneratorConfig(config);

        return generator;
    }
}