/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.joingo.yoga

import com.joingo.yoga.enums.YogaEdge
import com.joingo.yoga.enums.YogaUnit
import com.joingo.yoga.enums.YogaWrap
import com.joingo.yoga.enums.YogaAlign
import com.joingo.yoga.enums.YogaDisplay
import com.joingo.yoga.enums.YogaJustify
import com.joingo.yoga.enums.YogaOverflow
import com.joingo.yoga.enums.YogaDirection
import com.joingo.yoga.enums.YogaMeasureMode
import com.joingo.yoga.enums.YogaPositionType
import com.joingo.yoga.enums.YogaFlexDirection
import com.joingo.yoga.internal.enums.YGEdge
import com.joingo.yoga.internal.enums.YGWrap
import com.joingo.yoga.internal.enums.YGAlign
import com.joingo.yoga.internal.enums.YGDisplay
import com.joingo.yoga.internal.enums.YGJustify
import com.joingo.yoga.internal.enums.YGOverflow
import com.joingo.yoga.internal.enums.YGDirection
import com.joingo.yoga.internal.enums.YGMeasureMode
import com.joingo.yoga.internal.enums.YGPositionType
import com.joingo.yoga.internal.enums.YGFlexDirection
import com.joingo.yoga.internal.interfaces.YGMeasureFunc
import java.lang.CloneNotSupportedException
import java.lang.RuntimeException
import com.joingo.yoga.interfaces.YogaMeasureFunction
import com.joingo.yoga.interfaces.YogaBaselineFunction
import com.joingo.yoga.internal.*
import java.util.ArrayList

class YogaNodeWrapper private constructor(protected var mNativePointer: YGNode) : com.joingo.yoga.YogaNode(),
    Cloneable {
    private var mOwner: com.joingo.yoga.YogaNodeWrapper? = null
    private var mChildren: ArrayList<com.joingo.yoga.YogaNodeWrapper>? = null
    private var mMeasureFunction: YogaMeasureFunction? = null
    private var mBaselineFunction: YogaBaselineFunction? = null
    private var mData: Any? = null
    private val mLayoutDirection = 0

    internal constructor() : this(GlobalMembers.YGNodeNew()) {}
    internal constructor(config: com.joingo.yoga.YogaConfig) : this(GlobalMembers.YGNodeNewWithConfig((config as com.joingo.yoga.YogaConfigWrapper).mNativePointer)) {}

    override fun reset() {
        mMeasureFunction = null
        mBaselineFunction = null
        mData = null
        GlobalMembers.YGNodeReset(mNativePointer)
    }

    override fun getChildCount(): Int {
        return if (mChildren == null) 0 else mChildren!!.size
    }

    override fun getChildAt(i: Int): com.joingo.yoga.YogaNodeWrapper {
        checkNotNull(mChildren) { "YogaNode does not have children" }
        return mChildren!![i]
    }

    override fun addChildAt(c: com.joingo.yoga.YogaNode?, i: Int) {
        if (c !is com.joingo.yoga.YogaNodeWrapper) {
            return
        }
        val child = c
        check(child.mOwner == null) { "Child already has a parent, it must be removed first." }
        if (mChildren == null) {
            mChildren = ArrayList(4)
        }
        mChildren!!.add(i, child)
        child.mOwner = this
        GlobalMembers.YGNodeInsertChild(mNativePointer, child.mNativePointer, i)
    }

    override fun setIsReferenceBaseline(isReferenceBaseline: Boolean) {
        GlobalMembers.YGNodeSetIsReferenceBaseline(mNativePointer, isReferenceBaseline)
    }

    override fun isReferenceBaseline(): Boolean {
        return GlobalMembers.YGNodeIsReferenceBaseline(mNativePointer)
    }

    fun swapChildAt(newChild: com.joingo.yoga.YogaNode?, position: Int) {
        if (newChild !is com.joingo.yoga.YogaNodeWrapper) {
            return
        }
        val child = newChild
        mChildren!!.removeAt(position)
        mChildren!!.add(position, child)
        child.mOwner = this
        GlobalMembers.YGNodeSwapChild(mNativePointer, child.mNativePointer, position)
    }

    override fun cloneWithChildren(): com.joingo.yoga.YogaNodeWrapper {
        return try {
            val clonedYogaNode = super.clone() as com.joingo.yoga.YogaNodeWrapper
            if (clonedYogaNode.mChildren != null) {
                clonedYogaNode.mChildren = ArrayList(clonedYogaNode.mChildren)
            }
            val clonedNativePointer = GlobalMembers.YGNodeClone(mNativePointer)
            clonedYogaNode.mOwner = null
            clonedYogaNode.mNativePointer = clonedNativePointer
            for (i in 0 until clonedYogaNode.getChildCount()) {
                clonedYogaNode.swapChildAt(clonedYogaNode.getChildAt(i).cloneWithChildren(), i)
            }
            clonedYogaNode
        } catch (ex: CloneNotSupportedException) {
            // This class implements Cloneable, this should not happen
            throw RuntimeException(ex)
        }
    }

    override fun cloneWithoutChildren(): com.joingo.yoga.YogaNodeWrapper {
        return try {
            val clonedYogaNode = super.clone() as com.joingo.yoga.YogaNodeWrapper
            val clonedNativePointer = GlobalMembers.YGNodeClone(mNativePointer)
            clonedYogaNode.mOwner = null
            clonedYogaNode.mNativePointer = clonedNativePointer
            clonedYogaNode.clearChildren()
            clonedYogaNode
        } catch (ex: CloneNotSupportedException) {
            // This class implements Cloneable, this should not happen
            throw RuntimeException(ex)
        }
    }

    private fun clearChildren() {
        mChildren = null
        mNativePointer.clearChildren()
    }

    override fun removeChildAt(i: Int): com.joingo.yoga.YogaNodeWrapper {
        checkNotNull(mChildren) { "Trying to remove a child of a YogaNode that does not have children" }
        val child: com.joingo.yoga.YogaNodeWrapper = mChildren!!.removeAt(i)
        child.mOwner = null
        GlobalMembers.YGNodeRemoveChild(mNativePointer, child.mNativePointer)
        return child
    }

    /**
     * The owner is used to identify the YogaTree that a [YogaNode] belongs to. This method will
     * return the parent of the [YogaNode] when the [YogaNode] only belongs to one
     * YogaTree or null when the [YogaNode] is shared between two or more YogaTrees.
     *
     * @return the [YogaNode] that owns this [YogaNode].
     */
    override fun getOwner(): com.joingo.yoga.YogaNodeWrapper? {
        return mOwner
    }

    @Deprecated("Use #getOwner() instead. This will be removed in the next version.")
    override fun getParent(): com.joingo.yoga.YogaNodeWrapper? {
        return getOwner()
    }

    override fun indexOf(child: com.joingo.yoga.YogaNode?): Int {
        return if (mChildren == null) -1 else mChildren!!.indexOf(child)
    }

    override fun calculateLayout(width: Float, height: Float) {
        var nativePointers: Array<YGNode?>? = null
        var nodes: Array<com.joingo.yoga.YogaNodeWrapper>? = null
        freeze(null)
        val n = ArrayList<com.joingo.yoga.YogaNodeWrapper>()
        n.add(this)
        for (i in n.indices) {
            val parent = n[i]
            val children = parent.mChildren
            if (children != null) {
                for (child in children) {
                    child.freeze(parent)
                    n.add(child)
                }
            }
        }
        nodes = n.toTypedArray()
        nativePointers = arrayOfNulls(nodes.size)
        for (i in nodes.indices) {
            nativePointers[i] = nodes[i].mNativePointer
        }
        GlobalMembers.YGNodeCalculateLayoutWithContext(
            mNativePointer, width, height,
            GlobalMembers.YGNodeStyleGetDirection(mNativePointer)!!, nativePointers
        )
    }

    private fun freeze(parent: com.joingo.yoga.YogaNode?) {
        val data = getData()
        if (data is com.joingo.yoga.YogaNode.Inputs) {
            (data as com.joingo.yoga.YogaNode.Inputs).freeze(this, parent)
        }
    }

    override fun dirty() {
        GlobalMembers.YGNodeMarkDirty(mNativePointer)
    }

    fun dirtyAllDescendants() {
        GlobalMembers.YGNodeMarkDirtyAndPropogateToDescendants(mNativePointer)
    }

    override fun isDirty(): Boolean {
        return GlobalMembers.YGNodeIsDirty(mNativePointer)
    }

    override fun copyStyle(srcNode: com.joingo.yoga.YogaNode?) {
        if (srcNode !is com.joingo.yoga.YogaNodeWrapper) {
            return
        }
        GlobalMembers.YGNodeCopyStyle(mNativePointer, srcNode.mNativePointer)
    }

    override fun getStyleDirection(): YogaDirection? {
        return YogaDirection.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetDirection(mNativePointer)!!
                .getValue()
        )
    }

    override fun setDirection(direction: YogaDirection) {
        GlobalMembers.YGNodeStyleSetDirection(
            mNativePointer,
            YGDirection.Companion.forValue(direction.intValue())
        )
    }

    override fun getFlexDirection(): YogaFlexDirection? {
        return YogaFlexDirection.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetFlexDirection(
                mNativePointer
            )!!.getValue()
        )
    }

    override fun setFlexDirection(flexDirection: YogaFlexDirection) {
        GlobalMembers.YGNodeStyleSetFlexDirection(
            mNativePointer,
            YGFlexDirection.Companion.forValue(flexDirection.intValue())
        )
    }

    override fun getJustifyContent(): YogaJustify? {
        return YogaJustify.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetJustifyContent(
                mNativePointer
            )!!.getValue()
        )
    }

    override fun setJustifyContent(justifyContent: YogaJustify) {
        GlobalMembers.YGNodeStyleSetJustifyContent(
            mNativePointer,
            YGJustify.Companion.forValue(justifyContent.intValue())
        )
    }

    override fun getAlignItems(): YogaAlign? {
        return YogaAlign.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetAlignItems(mNativePointer)!!
                .getValue()
        )
    }

    override fun setAlignItems(alignItems: YogaAlign) {
        GlobalMembers.YGNodeStyleSetAlignItems(
            mNativePointer,
            YGAlign.Companion.forValue(alignItems.intValue())
        )
    }

    override fun getAlignSelf(): YogaAlign? {
        return YogaAlign.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetAlignSelf(mNativePointer)!!
                .getValue()
        )
    }

    override fun setAlignSelf(alignSelf: YogaAlign) {
        GlobalMembers.YGNodeStyleSetAlignSelf(
            mNativePointer,
            YGAlign.Companion.forValue(alignSelf.intValue())
        )
    }

    override fun getAlignContent(): YogaAlign? {
        return YogaAlign.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetAlignContent(mNativePointer)!!
                .getValue()
        )
    }

    override fun setAlignContent(alignContent: YogaAlign) {
        GlobalMembers.YGNodeStyleSetAlignContent(
            mNativePointer,
            YGAlign.Companion.forValue(alignContent.intValue())
        )
    }

    override fun getPositionType(): YogaPositionType? {
        return YogaPositionType.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetPositionType(mNativePointer)!!
                .getValue()
        )
    }

    override fun setPositionType(positionType: YogaPositionType) {
        GlobalMembers.YGNodeStyleSetPositionType(
            mNativePointer,
            YGPositionType.Companion.forValue(positionType.intValue())
        )
    }

    override fun getWrap(): YogaWrap {
        return YogaWrap.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetFlexWrap(mNativePointer)!!
                .getValue()
        )
    }

    override fun setWrap(flexWrap: YogaWrap) {
        GlobalMembers.YGNodeStyleSetFlexWrap(
            mNativePointer,
            YGWrap.Companion.forValue(flexWrap.intValue())
        )
    }

    override fun getOverflow(): YogaOverflow {
        return YogaOverflow.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetOverflow(mNativePointer)!!
                .getValue()
        )
    }

    override fun setOverflow(overflow: YogaOverflow) {
        GlobalMembers.YGNodeStyleSetOverflow(
            mNativePointer,
            YGOverflow.Companion.forValue(overflow.intValue())
        )
    }

    override fun getDisplay(): YogaDisplay {
        return YogaDisplay.Companion.fromInt(
            GlobalMembers.YGNodeStyleGetDisplay(mNativePointer)!!
                .getValue()
        )
    }

    override fun setDisplay(display: YogaDisplay) {
        GlobalMembers.YGNodeStyleSetDisplay(
            mNativePointer,
            YGDisplay.Companion.forValue(display.intValue())
        )
    }

    override fun getFlex(): Float {
        return GlobalMembers.YGNodeStyleGetFlex(mNativePointer)
    }

    override fun setFlex(flex: Float) {
        GlobalMembers.YGNodeStyleSetFlex(mNativePointer, flex)
    }

    override fun getFlexGrow(): Float {
        return GlobalMembers.YGNodeStyleGetFlexGrow(mNativePointer)
    }

    override fun setFlexGrow(flexGrow: Float) {
        GlobalMembers.YGNodeStyleSetFlexGrow(mNativePointer, flexGrow)
    }

    override fun getFlexShrink(): Float {
        return GlobalMembers.YGNodeStyleGetFlexShrink(mNativePointer)
    }

    override fun setFlexShrink(flexShrink: Float) {
        GlobalMembers.YGNodeStyleSetFlexShrink(mNativePointer, flexShrink)
    }

    override fun getFlexBasis(): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetFlexBasis(
                mNativePointer
            )
        )
    }

    override fun setFlexBasis(flexBasis: Float) {
        GlobalMembers.YGNodeStyleSetFlexBasis(mNativePointer, flexBasis)
    }

    override fun setFlexBasisPercent(percent: Float) {
        GlobalMembers.YGNodeStyleSetFlexBasisPercent(mNativePointer, percent)
    }

    override fun setFlexBasisAuto() {
        GlobalMembers.YGNodeStyleSetFlexBasisAuto(mNativePointer)
    }

    override fun getMargin(edge: YogaEdge): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetMargin(
                mNativePointer,
                YGEdge.Companion.forValue(edge.intValue())
            )
        )
    }

    override fun setMargin(edge: YogaEdge, margin: Float) {
        GlobalMembers.YGNodeStyleSetMargin(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue()),
            margin
        )
    }

    override fun setMarginPercent(edge: YogaEdge, percent: Float) {
        GlobalMembers.YGNodeStyleSetMarginPercent(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue()),
            percent
        )
    }

    override fun setMarginAuto(edge: YogaEdge) {
        GlobalMembers.YGNodeStyleSetMarginAuto(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue())
        )
    }

    override fun getPadding(edge: YogaEdge): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetPadding(
                mNativePointer,
                YGEdge.Companion.forValue(edge.intValue())
            )
        )
    }

    override fun setPadding(edge: YogaEdge, padding: Float) {
        GlobalMembers.YGNodeStyleSetPadding(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue()),
            padding
        )
    }

    override fun setPaddingPercent(edge: YogaEdge, percent: Float) {
        GlobalMembers.YGNodeStyleSetPaddingPercent(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue()),
            percent
        )
    }

    override fun getBorder(edge: YogaEdge): Float {
        return GlobalMembers.YGNodeStyleGetBorder(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue())
        )
    }

    override fun setBorder(edge: YogaEdge, border: Float) {
        GlobalMembers.YGNodeStyleSetBorder(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue()),
            border
        )
    }

    override fun getPosition(edge: YogaEdge): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetPosition(
                mNativePointer,
                YGEdge.Companion.forValue(edge.intValue())
            )
        )
    }

    override fun setPosition(edge: YogaEdge, position: Float) {
        GlobalMembers.YGNodeStyleSetPosition(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue()),
            position
        )
    }

    override fun setPositionPercent(edge: YogaEdge, percent: Float) {
        GlobalMembers.YGNodeStyleSetPositionPercent(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue()),
            percent
        )
    }

    override fun getWidth(): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetWidth(
                mNativePointer
            )
        )
    }

    override fun setWidth(width: Float) {
        GlobalMembers.YGNodeStyleSetWidth(mNativePointer, width)
    }

    override fun setWidthPercent(percent: Float) {
        GlobalMembers.YGNodeStyleSetWidthPercent(mNativePointer, percent)
    }

    override fun setWidthAuto() {
        GlobalMembers.YGNodeStyleSetWidthAuto(mNativePointer)
    }

    override fun getHeight(): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetHeight(
                mNativePointer
            )
        )
    }

    override fun setHeight(height: Float) {
        GlobalMembers.YGNodeStyleSetHeight(mNativePointer, height)
    }

    override fun setHeightPercent(percent: Float) {
        GlobalMembers.YGNodeStyleSetHeightPercent(mNativePointer, percent)
    }

    override fun setHeightAuto() {
        GlobalMembers.YGNodeStyleSetHeightAuto(mNativePointer)
    }

    override fun getMinWidth(): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetMinWidth(
                mNativePointer
            )
        )
    }

    override fun setMinWidth(minWidth: Float) {
        GlobalMembers.YGNodeStyleSetMinWidth(mNativePointer, minWidth)
    }

    override fun setMinWidthPercent(percent: Float) {
        GlobalMembers.YGNodeStyleSetMinWidthPercent(mNativePointer, percent)
    }

    override fun getMinHeight(): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetMinHeight(
                mNativePointer
            )
        )
    }

    override fun setMinHeight(minHeight: Float) {
        GlobalMembers.YGNodeStyleSetMinHeight(mNativePointer, minHeight)
    }

    override fun setMinHeightPercent(percent: Float) {
        GlobalMembers.YGNodeStyleSetMinHeightPercent(mNativePointer, percent)
    }

    override fun getMaxWidth(): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetMaxWidth(
                mNativePointer
            )
        )
    }

    override fun setMaxWidth(maxWidth: Float) {
        GlobalMembers.YGNodeStyleSetMaxWidth(mNativePointer, maxWidth)
    }

    override fun setMaxWidthPercent(percent: Float) {
        GlobalMembers.YGNodeStyleSetMaxWidthPercent(mNativePointer, percent)
    }

    override fun getMaxHeight(): com.joingo.yoga.YogaValue? {
        return com.joingo.yoga.YogaNodeWrapper.Companion.valueFromNative(
            GlobalMembers.YGNodeStyleGetMaxHeight(
                mNativePointer
            )
        )
    }

    override fun setMaxHeight(maxheight: Float) {
        GlobalMembers.YGNodeStyleSetMaxHeight(mNativePointer, maxheight)
    }

    override fun setMaxHeightPercent(percent: Float) {
        GlobalMembers.YGNodeStyleSetMaxHeightPercent(mNativePointer, percent)
    }

    override fun getAspectRatio(): Float {
        return GlobalMembers.YGNodeStyleGetAspectRatio(mNativePointer)
    }

    override fun setAspectRatio(aspectRatio: Float) {
        GlobalMembers.YGNodeStyleSetAspectRatio(mNativePointer, aspectRatio)
    }

    override fun setMeasureFunction(measureFunction: YogaMeasureFunction?) {
        mMeasureFunction = measureFunction
        mNativePointer.getMeasure().noContext =
            YGMeasureFunc { node: YGNode?, width: Float, widthMode: YGMeasureMode?, height: Float, heightMode: YGMeasureMode? ->
                measure(
                    width,
                    widthMode!!.getValue(), height, heightMode!!.getValue()
                )!!
            }
    }

    // Implementation Note: Why this method needs to stay final
    //
    // We cache the jmethodid for this method in Yoga code. This means that even if a subclass
    // were to override measure, we'd still call this implementation from layout code since the
    // overriding method will have a different jmethodid. This is final to prevent that mistake.
    fun measure(width: Float, widthMode: Int, height: Float, heightMode: Int): YGSize? {
        if (!isMeasureDefined()) {
            throw RuntimeException("Measure function isn't defined!")
        }
        return mMeasureFunction!!.measure(
            this,
            width,
            YogaMeasureMode.Companion.fromInt(widthMode),
            height,
            YogaMeasureMode.Companion.fromInt(heightMode)
        )
    }

    override fun setBaselineFunction(baselineFunction: YogaBaselineFunction?) {
        mBaselineFunction = baselineFunction
        mNativePointer.setBaselineFunc { node: YGNode?, width: Float, height: Float ->
            baseline(
                width,
                height
            )
        }
    }

    fun baseline(width: Float, height: Float): Float {
        return mBaselineFunction!!.baseline(this, width, height)
    }

    override fun isMeasureDefined(): Boolean {
        return mMeasureFunction != null
    }

    override fun isBaselineDefined(): Boolean {
        return mBaselineFunction != null
    }

    override fun getData(): Any? {
        return mData
    }

    override fun setData(data: Any?) {
        mData = data
    }

    /**
     * Use the set logger (defaults to adb log) to print out the styles, children, and computed layout
     * of the tree rooted at this node.
     */
    override fun print() {
        //TODO: GlobalMembers.YGNodePrint(mNativePointer);
        // Couldn't find the print method lmao
    }

    /**
     * This method replaces the child at childIndex position with the newNode received by parameter.
     * This is different than calling removeChildAt and addChildAt because this method ONLY replaces
     * the child in the mChildren datastructure. : called from JNI
     *
     * @return the nativePointer of the newNode [YogaNode]
     */
    private fun replaceChild(newNode: com.joingo.yoga.YogaNodeWrapper, childIndex: Int): YGNode {
        checkNotNull(mChildren) { "Cannot replace child. YogaNode does not have children" }
        mChildren!!.removeAt(childIndex)
        mChildren!!.add(childIndex, newNode)
        newNode.mOwner = this
        return newNode.mNativePointer
    }

    override fun getLayoutX(): Float {
        return GlobalMembers.YGNodeLayoutGetLeft(mNativePointer)
    }

    override fun getLayoutY(): Float {
        return GlobalMembers.YGNodeLayoutGetTop(mNativePointer)
    }

    override fun getLayoutWidth(): Float {
        return GlobalMembers.YGNodeLayoutGetWidth(mNativePointer)
    }

    override fun getLayoutHeight(): Float {
        return GlobalMembers.YGNodeLayoutGetHeight(mNativePointer)
    }

    fun getDoesLegacyStretchFlagAffectsLayout(): Boolean {
        return GlobalMembers.YGNodeLayoutGetDidLegacyStretchFlagAffectLayout(mNativePointer)
    }

    override fun getLayoutMargin(edge: YogaEdge): Float {
        return GlobalMembers.YGNodeLayoutGetMargin(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue())
        )
    }

    override fun getLayoutPadding(edge: YogaEdge): Float {
        return GlobalMembers.YGNodeLayoutGetPadding(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue())
        )
    }

    override fun getLayoutBorder(edge: YogaEdge): Float {
        return GlobalMembers.YGNodeLayoutGetBorder(
            mNativePointer,
            YGEdge.Companion.forValue(edge.intValue())
        )
    }

    override fun getLayoutDirection(): YogaDirection {
        return YogaDirection.Companion.fromInt(
            GlobalMembers.YGNodeLayoutGetDirection(mNativePointer)!!
                .getValue()
        )
    }

    override fun hasNewLayout(): Boolean {
        return GlobalMembers.YGNodeGetHasNewLayout(mNativePointer)
    }

    override fun markLayoutSeen() {
        GlobalMembers.YGNodeSetHasNewLayout(mNativePointer, false)
    }

    companion object {
        /* Those flags needs be in sync with YGJNI.h */
        private const val MARGIN: Byte = 1
        private const val PADDING: Byte = 2
        private const val BORDER: Byte = 4
        private const val DOES_LEGACY_STRETCH_BEHAVIOUR: Byte = 8
        private const val HAS_NEW_LAYOUT: Byte = 16
        private const val LAYOUT_EDGE_SET_FLAG_INDEX: Byte = 0
        private const val LAYOUT_WIDTH_INDEX: Byte = 1
        private const val LAYOUT_HEIGHT_INDEX: Byte = 2
        private const val LAYOUT_LEFT_INDEX: Byte = 3
        private const val LAYOUT_TOP_INDEX: Byte = 4
        private const val LAYOUT_DIRECTION_INDEX: Byte = 5
        private const val LAYOUT_MARGIN_START_INDEX: Byte = 6
        private const val LAYOUT_PADDING_START_INDEX: Byte = 10
        private const val LAYOUT_BORDER_START_INDEX: Byte = 14
        private fun valueFromNative(value: YGValue?): com.joingo.yoga.YogaValue {
            return com.joingo.yoga.YogaValue(
                value!!.value,
                YogaUnit.Companion.fromInt(value.unit!!.getValue())
            )
        }
    }
}