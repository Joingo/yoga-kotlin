/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga

import com.joingo.yoga.enums.YogaExperimentalFeature
import com.joingo.yoga.interfaces.YogaLogger
import com.joingo.yoga.internal.GlobalMembers
import com.joingo.yoga.internal.YGConfig
import com.joingo.yoga.internal.enums.YGExperimentalFeature

class YogaConfigWrapper private constructor(var mNativePointer: YGConfig) :
    com.joingo.yoga.YogaConfig() {
    private var mLogger: YogaLogger? = null

    internal constructor() : this(GlobalMembers.YGConfigNew()) {}

    override fun setExperimentalFeatureEnabled(feature: YogaExperimentalFeature, enabled: Boolean) {
        GlobalMembers.YGConfigSetExperimentalFeatureEnabled(
            mNativePointer,
            YGExperimentalFeature.Companion.forValue(feature.intValue()), enabled
        )
    }

    override fun setUseWebDefaults(useWebDefaults: Boolean) {
        GlobalMembers.YGConfigSetUseWebDefaults(mNativePointer, useWebDefaults)
    }

    override fun setPrintTreeFlag(enable: Boolean) {
        GlobalMembers.YGConfigSetPrintTreeFlag(mNativePointer, enable)
    }

    override fun setPointScaleFactor(pixelsInPoint: Float) {
        GlobalMembers.YGConfigSetPointScaleFactor(mNativePointer, pixelsInPoint)
    }

    /**
     * Yoga previously had an error where containers would take the maximum space possible instead of the minimum
     * like they are supposed to. In practice this resulted in implicit behaviour similar to align-self: stretch;
     * Because this was such a long-standing bug we must allow legacy users to switch back to this behaviour.
     */
    override fun setUseLegacyStretchBehaviour(useLegacyStretchBehaviour: Boolean) {
        GlobalMembers.YGConfigSetUseLegacyStretchBehaviour(
            mNativePointer,
            useLegacyStretchBehaviour
        )
    }

    /**
     * If this flag is set then yoga would diff the layout without legacy flag and would set a bool in
     * YogaNode(mDoesLegacyStretchFlagAffectsLayout) with true if the layouts were different and false
     * if not
     */
    override fun setShouldDiffLayoutWithoutLegacyStretchBehaviour(
        shouldDiffLayoutWithoutLegacyStretchBehaviour: Boolean
    ) {
        GlobalMembers.YGConfigSetShouldDiffLayoutWithoutLegacyStretchBehaviour(
            mNativePointer, shouldDiffLayoutWithoutLegacyStretchBehaviour
        )
    }

    override fun setLogger(logger: YogaLogger?) {
        mLogger = logger
        GlobalMembers.YGConfigSetLogger(mNativePointer, logger)
    }

    override fun getLogger(): YogaLogger? {
        return mLogger
    }

    public override fun getNativePointer(): YGConfig {
        return mNativePointer
    }
}