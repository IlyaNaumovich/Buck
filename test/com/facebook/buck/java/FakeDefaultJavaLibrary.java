/*
 * Copyright 2012-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.java;

import com.facebook.buck.rules.BuildRule;
import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.rules.BuildRuleResolver;
import com.facebook.buck.rules.FakeBuildRuleBuilderParams;
import com.facebook.buck.rules.SourcePath;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.nio.file.Path;
import java.util.Set;

public class FakeDefaultJavaLibrary extends DefaultJavaLibrary {

  // TODO(mbolin): Find a way to make use of this field or delete it.
  @SuppressWarnings("unused")
  private final boolean ruleInputsAreCached;

  protected FakeDefaultJavaLibrary(
      BuildRuleParams buildRuleParams,
      Set<Path> srcs,
      Set<SourcePath> resources,
      Optional<Path> proguardConfig,
      AnnotationProcessingParams annotationProcessingParams,
      Set<BuildRule> exportedDeps,
      boolean ruleInputsAreCached) {
    super(buildRuleParams,
        srcs,
        resources,
        proguardConfig,
        ImmutableList.<String>of(),
        exportedDeps,
        /* addtionalClasspathEntries */ ImmutableSet.<String>of(),
        JavacOptions.builder()
            .setAnnotationProcessingData(annotationProcessingParams)
            .build());

    this.ruleInputsAreCached = ruleInputsAreCached;
  }

  public static FakeDefaultJavaLibrary.Builder newFakeJavaLibraryRuleBuilder() {
    return new FakeDefaultJavaLibrary.Builder();
  }

  public static class Builder extends DefaultJavaLibrary.Builder {
    private boolean ruleInputsAreCached;

    public Builder() {
      super(new FakeBuildRuleBuilderParams());
    }

    @Override
    public FakeDefaultJavaLibrary build(BuildRuleResolver ruleResolver) {
      BuildRuleParams buildRuleParams = createBuildRuleParams(ruleResolver);
      AnnotationProcessingParams processingParams =
          annotationProcessingBuilder.build(ruleResolver);

      return new FakeDefaultJavaLibrary(
          buildRuleParams,
          srcs,
          resources,
          proguardConfig,
          processingParams,
          getBuildTargetsAsBuildRules(ruleResolver, exportedDeps),
          ruleInputsAreCached);
    }

    public Builder setRuleInputsAreCached(boolean ruleInputsAreCached) {
      this.ruleInputsAreCached = ruleInputsAreCached;
      return this;
    }
  }
}
