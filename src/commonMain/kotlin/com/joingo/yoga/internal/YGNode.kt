package com.joingo.yoga.internal

import com.joingo.yoga.internal.enums.YGEdge
import com.joingo.yoga.internal.enums.YGUnit
import com.joingo.yoga.internal.enums.YGAlign
import com.joingo.yoga.internal.enums.YGNodeType
import com.joingo.yoga.internal.enums.YGDimension
import com.joingo.yoga.internal.enums.YGDirection
import com.joingo.yoga.internal.enums.YGMeasureMode
import com.joingo.yoga.internal.enums.YGPositionType
import com.joingo.yoga.internal.enums.YGFlexDirection
import java.util.HashMap
import com.joingo.yoga.internal.detail.CompactValue
import java.util.Arrays
import com.joingo.yoga.internal.interfaces.YGDirtiedFunc
import com.joingo.yoga.internal.interfaces.YGBaselineFunc
import com.joingo.yoga.internal.interfaces.BaselineWithContextFn
import com.joingo.yoga.internal.interfaces.YGPrintFunc
import com.joingo.yoga.internal.interfaces.PrintWithContextFn
import com.joingo.yoga.internal.interfaces.YGMeasureFunc
import com.joingo.yoga.internal.interfaces.MeasureWithContextFn
import java.util.function.BiConsumer
import com.joingo.yoga.internal.detail.Values
import java.util.ArrayList
import java.util.function.Consumer

class YGNode {
    private var context_: Any? = null
    private var flags: MutableMap<Any?, Any> = HashMap()
    private var reserved_: Byte = 0
    private var measure_ = measure_Struct()
    private var baseline_ = baseline_Struct()
    private var print_ = print_Struct()
    private var dirtied_: YGDirtiedFunc? = null
    private var style_: YGStyle = YGStyle()
    private var layout_: YGLayout? = YGLayout()
    private var lineIndex_ = 0
    private var owner_: YGNode? = null
    private var children_ = ArrayList<YGNode>()
    private var config_: YGConfig? = YGConfig(null)
    private var resolvedDimensions_ = ArrayList(
        Arrays.asList(GlobalMembers.YGValueUndefined, GlobalMembers.YGValueUndefined)
    )

    constructor(node: YGNode) {
        context_ = node.context_
        flags = node.flags
        measure_ = node.measure_
        baseline_ = node.baseline_
        print_ = node.print_
        dirtied_ = node.dirtied_
        style_ = node.style_
        layout_ = node.layout_
        lineIndex_ = node.lineIndex_
        owner_ = node.owner_
        children_ = node.children_ //TODO: Make full copy
        config_ = node.config_
        resolvedDimensions_ = node.resolvedDimensions_
        for (c in children_) {
            c.setOwner(this)
        }
    }

    constructor(node: YGNode, config: YGConfig) : this(node) {
        config_ = config
        if (config.useWebDefaults) {
            useWebDefaults()
        }
    }

    constructor(config: YGConfig) {
        config_ = config
        if (config.useWebDefaults) {
            useWebDefaults()
        }
    }

    fun getResolvedDimension(index: Int): YGValue? {
        return resolvedDimensions_[index]
    }

    fun isDirty(): Boolean {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            isDirty_
        )
    }

    fun setDirty(isDirty: Boolean) {
        if (isDirty == com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                flags,
                isDirty_
            )
        ) {
            return
        }
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            isDirty_,
            isDirty
        )
        if (isDirty && dirtied_ != null) {
            dirtied_!!.invoke(this)
        }
    }

    fun hasBaselineFunc(): Boolean {
        return if (baseline_ != null) {
            baseline_.noContext != null
        } else false
    }

    fun setBaselineFunc(baseLineFunc: YGBaselineFunc?) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            baselineUsesContext_,
            false
        )
        if (baseline_ != null) {
            baseline_.noContext = baseLineFunc
        }
    }

    fun setBaselineFunc(baseLineFunc: BaselineWithContextFn?) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            baselineUsesContext_,
            true
        )
        if (baseline_ != null) {
            baseline_.withContext = baseLineFunc
        }
    }

    fun resetBaselineFunc() {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            baselineUsesContext_,
            false
        )
        if (baseline_ != null) {
            baseline_.noContext = null
        }
    }

    fun setDirtiedFunc(dirtiedFunc: YGDirtiedFunc?) {
        dirtied_ = dirtiedFunc
    }

    fun setPrintFunc(printFunc: YGPrintFunc?) {
        if (print_ != null) {
            print_.noContext = printFunc
        }
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            printUsesContext_,
            false
        )
    }

    fun setPrintFunc(printFunc: PrintWithContextFn?) {
        if (print_ != null) {
            print_.withContext = printFunc
        }
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            printUsesContext_,
            true
        )
    }

    fun resetPrintFunc() {
        if (print_ != null) {
            print_.noContext = null
        }
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            printUsesContext_,
            false
        )
    }

    fun getHasNewLayout(): Boolean {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            hasNewLayout_
        )
    }

    fun setHasNewLayout(hasNewLayout: Boolean) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            hasNewLayout_,
            hasNewLayout
        )
    }

    fun getNodeType(): YGNodeType {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getEnumData<YGNodeType>(
            YGNodeType::class.java, flags, nodeType_
        )
    }

    fun setNodeType(nodeType: YGNodeType) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setEnumData<YGNodeType>(
            YGNodeType::class.java, flags, nodeType_, nodeType
        )
    }

    fun setIsReferenceBaseline(isReferenceBaseline: Boolean) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            isReferenceBaseline_,
            isReferenceBaseline
        )
    }

    fun isReferenceBaseline(): Boolean {
        return com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
            flags,
            isReferenceBaseline_
        )
    }

    fun getChild(index: Int?): YGNode {
        return children_[index!!]
    }

    fun hasMeasureFunc(): Boolean {
        return if (measure_ != null) {
            measure_.noContext != null
        } else false
    }

    private fun useWebDefaults() {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            useWebDefaults_,
            true
        )
        style_!!.flexDirectionBitfieldRef().setValue(YGFlexDirection.YGFlexDirectionRow)
        style_!!.alignContentBitfieldRef().setValue(YGAlign.YGAlignStretch)
    }

    fun print(printContext: Any?) {
        if (print_.noContext != null) {
            if (com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                    flags,
                    printUsesContext_
                )
            ) {
                print_.withContext!!.invoke(this, printContext)
            } else {
                print_.noContext!!.invoke(this)
            }
        }
    }

    fun getLeadingPosition(axis: YGFlexDirection, axisSize: Float): YGFloatOptional {
        val leadingPosition = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.position(), YGEdge.YGEdgeStart,
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.position(),
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        )
        return GlobalMembers.YGResolveValue(leadingPosition, axisSize)
    }

    fun getTrailingPosition(axis: YGFlexDirection, axisSize: Float): YGFloatOptional {
        val trailingPosition = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.position(), YGEdge.YGEdgeEnd,
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.position(),
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        )
        return GlobalMembers.YGResolveValue(trailingPosition, axisSize)
    }

    fun isLeadingPositionDefined(axis: YGFlexDirection): Boolean {
        val leadingPosition = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.position(), YGEdge.YGEdgeStart,
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofUndefined()
        ) else computeEdgeValueForColumn(
            style_!!.position(),
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofUndefined()
        )
        return !leadingPosition.isUndefined()
    }

    fun isTrailingPosDefined(axis: YGFlexDirection): Boolean {
        val trailingPosition = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.position(), YGEdge.YGEdgeEnd,
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofUndefined()
        ) else computeEdgeValueForColumn(
            style_!!.position(),
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofUndefined()
        )
        return !trailingPosition.isUndefined()
    }

    fun getLeadingMargin(axis: YGFlexDirection, widthSize: Float): YGFloatOptional {
        val leadingMargin = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.margin(), YGEdge.YGEdgeStart,
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.margin(),
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        )
        return GlobalMembers.YGResolveValueMargin(leadingMargin, widthSize)
    }

    fun getTrailingMargin(axis: YGFlexDirection, widthSize: Float): YGFloatOptional {
        val trailingMargin = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.margin(), YGEdge.YGEdgeEnd,
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.margin(),
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        )
        return GlobalMembers.YGResolveValueMargin(trailingMargin, widthSize)
    }

    fun getMarginForAxis(axis: YGFlexDirection, widthSize: Float): YGFloatOptional {
        return GlobalMembers.plus(
            getLeadingMargin(axis, widthSize),
            getTrailingMargin(axis, widthSize)
        )
    }

    fun measure(
        width: Float,
        widthMode: YGMeasureMode?,
        height: Float,
        heightMode: YGMeasureMode?,
        layoutContext: Any?
    ): YGSize {
        return if (com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                flags,
                measureUsesContext_
            )
        ) measure_.withContext!!.invoke(
            this, width, widthMode, height,
            heightMode, layoutContext
        ) else measure_.noContext!!.invoke(this, width, widthMode, height, heightMode)
    }

    fun baseline(width: Float, height: Float, layoutContext: Any?): Float {
        return if (com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                flags,
                baselineUsesContext_
            )
        ) baseline_.withContext!!.invoke(
            this, width, height,
            layoutContext
        ) else baseline_.noContext!!.invoke(this, width, height)
    }

    fun setMeasureFunc(measureFunc: measure_Struct) {
        if (measureFunc.noContext == null) {
            setNodeType(YGNodeType.YGNodeTypeDefault)
        } else {
            GlobalMembers.YGAssertWithNode(
                this, children_.size == 0,
                "Cannot set measure function: Nodes with measure functions cannot have " + "children."
            )
            setNodeType(YGNodeType.YGNodeTypeText)
        }
        measure_ = measureFunc
    }

    fun setMeasureFunc(measureFunc: YGMeasureFunc?) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            measureUsesContext_,
            false
        )
        measure_.noContext = measureFunc
        setMeasureFunc(measure_)
    }

    fun setMeasureFunc(measureFunc: MeasureWithContextFn?) {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            measureUsesContext_,
            true
        )
        measure_.withContext = measureFunc
        setMeasureFunc(measure_)
    }

    fun replaceChild(child: YGNode, index: Int?) {
        children_[index!!] = child
    }

    fun replaceChild(oldChild: YGNode, newChild: YGNode) {
        children_.replaceAll { ygNode: YGNode -> if (ygNode == oldChild) newChild else ygNode }
    }

    fun insertChild(child: YGNode, index: Int?) {
        children_.add(index!!, child)
    }

    fun removeChild(child: YGNode): Boolean {
        return children_.remove(child)
    }

    fun removeChild(index: Int) {
        children_.removeAt(index)
    }

    fun setLayoutDirection(direction: YGDirection?) {
        layout_!!.setDirection(direction!!)
    }

    fun setLayoutMargin(margin: Float, index: Int) {
        layout_!!.margin[index] = margin
    }

    fun setLayoutBorder(border: Float, index: Int) {
        layout_!!.border[index] = border
    }

    fun setLayoutPadding(padding: Float, index: Int) {
        layout_!!.padding[index] = padding
    }

    fun setLayoutLastOwnerDirection(direction: YGDirection?) {
        layout_!!.lastOwnerDirection = direction!!
    }

    fun setLayoutComputedFlexBasis(computedFlexBasis: YGFloatOptional?) {
        if (layout_ != null) {
            layout_!!.computedFlexBasis = computedFlexBasis!!
        }
    }

    fun setLayoutPosition(position: Float, index: Int) {
        layout_!!.position[index] = position
    }

    fun setLayoutComputedFlexBasisGeneration(computedFlexBasisGeneration: Int) {
        layout_!!.computedFlexBasisGeneration = computedFlexBasisGeneration
    }

    fun setLayoutMeasuredDimension(measuredDimension: Float, index: Int) {
        layout_!!.measuredDimensions[index] = measuredDimension
    }

    fun setLayoutHadOverflow(hadOverflow: Boolean) {
        layout_!!.setHadOverflow(hadOverflow)
    }

    fun setLayoutDimension(dimension: Float, index: Int) {
        layout_!!.dimensions[index] = dimension
    }

    fun relativePosition(axis: YGFlexDirection, axisSize: Float): YGFloatOptional {
        if (isLeadingPositionDefined(axis)) {
            return getLeadingPosition(axis, axisSize)
        }
        var trailingPosition = getTrailingPosition(axis, axisSize)
        if (!trailingPosition.isUndefined()) {
            trailingPosition = YGFloatOptional(-1 * trailingPosition.unwrap())
        }
        return trailingPosition
    }

    fun setPosition(direction: YGDirection, mainSize: Float, crossSize: Float, ownerWidth: Float) {
        val directionRespectingRoot = if (owner_ != null) direction else YGDirection.YGDirectionLTR
        val mainAxis = GlobalMembers.YGResolveFlexDirection(
            style_!!.flexDirection(), directionRespectingRoot
        )
        val crossAxis = GlobalMembers.YGFlexDirectionCross(mainAxis, directionRespectingRoot)
        val relativePositionMain = relativePosition(mainAxis!!, mainSize)
        val relativePositionCross = relativePosition(crossAxis, crossSize)
        setLayoutPosition(
            GlobalMembers.plus(
                getLeadingMargin(
                    mainAxis, ownerWidth
                ), relativePositionMain
            ).unwrap(),
            GlobalMembers.leading[mainAxis.getValue()].getValue()
        )
        setLayoutPosition(
            GlobalMembers.plus(
                getTrailingMargin(
                    mainAxis, ownerWidth
                ), relativePositionMain
            ).unwrap(),
            GlobalMembers.trailing[mainAxis.getValue()].getValue()
        )
        setLayoutPosition(
            GlobalMembers.plus(getLeadingMargin(crossAxis, ownerWidth), relativePositionCross)
                .unwrap(),
            GlobalMembers.leading[crossAxis.getValue()].getValue()
        )
        setLayoutPosition(
            GlobalMembers.plus(getTrailingMargin(crossAxis, ownerWidth), relativePositionCross)
                .unwrap(),
            GlobalMembers.trailing[crossAxis.getValue()].getValue()
        )
    }

    fun marginLeadingValue(axis: YGFlexDirection): YGValue? {
        return if (GlobalMembers.YGFlexDirectionIsRow(axis) && !style_!!.margin()
                .getCompactValue(YGEdge.YGEdgeStart).isUndefined()
        ) {
            style_!!.margin()[YGEdge.YGEdgeStart.getValue()]
        } else {
            style_!!.margin()[GlobalMembers.leading[axis.getValue()].getValue()]
        }
    }

    fun marginTrailingValue(axis: YGFlexDirection): YGValue? {
        return if (GlobalMembers.YGFlexDirectionIsRow(axis) && !style_!!.margin()
                .getCompactValue(YGEdge.YGEdgeEnd).isUndefined()
        ) {
            style_!!.margin()[YGEdge.YGEdgeEnd.getValue()]
        } else {
            style_!!.margin()[GlobalMembers.trailing[axis.getValue()].getValue()]
        }
    }

    fun resolveFlexBasisPtr(): YGValue? {
        val flexBasis = style_!!.flexBasis().convertToYgValue()
        if (flexBasis!!.unit != YGUnit.YGUnitAuto && flexBasis.unit != YGUnit.YGUnitUndefined) {
            return flexBasis
        }
        return if (!style_!!.flex().isUndefined() && style_!!.flex().unwrap() > 0.0f) {
            if (com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                    flags,
                    useWebDefaults_
                )
            ) GlobalMembers.YGValueAuto else GlobalMembers.YGValueZero
        } else GlobalMembers.YGValueAuto
    }

    fun resolveDimension() {
        val style = getStyle()
        val dimensions = arrayOf(YGDimension.YGDimensionWidth, YGDimension.YGDimensionHeight)
        for (dim in dimensions) {
            if (!style!!.maxDimensions()
                    .getCompactValue(dim.getValue()).isUndefined() && GlobalMembers.YGValueEqual(
                    style.maxDimensions().getCompactValue(dim.getValue()),
                    style.minDimensions().getCompactValue(dim.getValue())
                )
            ) {
                resolvedDimensions_[dim.getValue()] = style.maxDimensions()[dim.getValue()]
            } else {
                resolvedDimensions_[dim.getValue()] = style.dimensions()[dim.getValue()]
            }
        }
    }

    fun resolveDirection(ownerDirection: YGDirection): YGDirection? {
        return if (style_!!.direction() == YGDirection.YGDirectionInherit) {
            if (ownerDirection.getValue() > YGDirection.YGDirectionInherit.getValue()) ownerDirection else YGDirection.YGDirectionLTR
        } else {
            style_!!.direction()
        }
    }

    fun clearChildren() {
        children_.clear()
    }

    fun cloneChildrenIfNeeded(cloneContext: Any?) {
        iterChildrenAfterCloningIfNeeded<Any>(
            { UnnamedParameter: YGNode?, UnnamedParameter2: Any? -> },
            cloneContext
        )
    }

    fun <T> iterChildrenAfterCloningIfNeeded(
        callback: BiConsumer<YGNode, Any?>,
        cloneContext: Any?
    ) {
        var i = 0
        for (child in children_) {
            var c = child
            if (child.getOwner() !== this) {
                c = config_!!.cloneNode(child, this, i, cloneContext)
                child.setOwner(this)
            }
            i += 1
            callback.accept(c, cloneContext)
        }
    }

    fun markDirtyAndPropogate() {
        if (!com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                flags,
                isDirty_
            )
        ) {
            setDirty(true)
            setLayoutComputedFlexBasis(YGFloatOptional())
            if (owner_ != null) {
                owner_!!.markDirtyAndPropogate()
            }
        }
    }

    fun markDirtyAndPropogateDownwards() {
        com.joingo.yoga.internal.detail.GlobalMembers.Companion.setBooleanData(
            flags,
            isDirty_,
            true
        )
        children_.forEach(Consumer { obj: YGNode -> obj.markDirtyAndPropogateDownwards() })
    }

    fun resolveFlexGrow(): Float {
        if (owner_ == null) {
            return 0.0f
        }
        if (!style_!!.flexGrow().isUndefined()) {
            return style_!!.flexGrow().unwrap()
        }
        return if (!style_!!.flex().isUndefined() && style_!!.flex().unwrap() > 0.0f) {
            style_!!.flex().unwrap()
        } else GlobalMembers.kDefaultFlexGrow
    }

    fun resolveFlexShrink(): Float {
        if (owner_ == null) {
            return 0.0f
        }
        if (!style_!!.flexShrink().isUndefined()) {
            return style_!!.flexShrink().unwrap()
        }
        if (!com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                flags,
                useWebDefaults_
            ) && !style_!!.flex().isUndefined() && style_!!.flex().unwrap() < 0.0f
        ) {
            return -style_!!.flex().unwrap()
        }
        return if (com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                flags,
                useWebDefaults_
            )
        ) GlobalMembers.kWebDefaultFlexShrink else GlobalMembers.kDefaultFlexShrink
    }

    fun isNodeFlexible(): Boolean {
        return style_!!.positionType() != YGPositionType.YGPositionTypeAbsolute && (resolveFlexGrow() != 0f || resolveFlexShrink() != 0f)
    }

    fun getLeadingBorder(axis: YGFlexDirection): Float {
        val leadingBorder = (if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.border(),
            YGEdge.YGEdgeStart,
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.border(),
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        )).convertToYgValue()
        return Math.max(leadingBorder!!.value, 0.0f)
    }

    fun getTrailingBorder(axis: YGFlexDirection): Float {
        val trailingBorder = (if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.border(), YGEdge.YGEdgeEnd,
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.border(),
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        )).convertToYgValue()
        return Math.max(trailingBorder!!.value, 0.0f)
    }

    fun getLeadingPadding(axis: YGFlexDirection, widthSize: Float): YGFloatOptional {
        val leadingPadding = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.padding(), YGEdge.YGEdgeStart,
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.padding(),
            GlobalMembers.leading[axis.getValue()], CompactValue.Companion.ofZero()
        )
        return GlobalMembers.YGFloatOptionalMax(
            GlobalMembers.YGResolveValue(
                leadingPadding,
                widthSize
            ), YGFloatOptional(0.0f)
        )
    }

    fun getTrailingPadding(axis: YGFlexDirection, widthSize: Float): YGFloatOptional {
        val trailingPadding = if (GlobalMembers.YGFlexDirectionIsRow(axis)) computeEdgeValueForRow(
            style_!!.padding(), YGEdge.YGEdgeEnd,
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        ) else computeEdgeValueForColumn(
            style_!!.padding(),
            GlobalMembers.trailing[axis.getValue()], CompactValue.Companion.ofZero()
        )
        return GlobalMembers.YGFloatOptionalMax(
            GlobalMembers.YGResolveValue(
                trailingPadding,
                widthSize
            ), YGFloatOptional(0.0f)
        )
    }

    fun getLeadingPaddingAndBorder(axis: YGFlexDirection, widthSize: Float): YGFloatOptional {
        return GlobalMembers.plus(
            getLeadingPadding(axis, widthSize),
            YGFloatOptional(getLeadingBorder(axis))
        )
    }

    fun getTrailingPaddingAndBorder(axis: YGFlexDirection, widthSize: Float): YGFloatOptional {
        return GlobalMembers.plus(
            getTrailingPadding(axis, widthSize),
            YGFloatOptional(getTrailingBorder(axis))
        )
    }

    fun didUseLegacyFlag(): Boolean {
        var didUseLegacyFlag = layout_!!.didUseLegacyFlag()
        if (didUseLegacyFlag) {
            return true
        }
        for (child in children_) {
            if (child.layout_!!.didUseLegacyFlag()) {
                didUseLegacyFlag = true
                break
            }
        }
        return didUseLegacyFlag
    }

    fun setLayoutDoesLegacyFlagAffectsLayout(doesLegacyFlagAffectsLayout: Boolean) {
        layout_!!.setDoesLegacyStretchFlagAffectsLayout(doesLegacyFlagAffectsLayout)
    }

    fun setLayoutDidUseLegacyFlag(didUseLegacyFlag: Boolean) {
        layout_!!.setDidUseLegacyFlag(didUseLegacyFlag)
    }

    fun isLayoutTreeEqualToNode(node: YGNode): Boolean {
        if (children_.size != node.children_.size) {
            return false
        }
        if (layout_ !== node.layout_) {
            return false
        }
        if (children_.size == 0) {
            return true
        }
        var isLayoutTreeEqual = true
        var i = 0
        val children_size = children_.size
        while (i < children_size) {
            val child = children_[i]
            val otherNodeChildren = node.children_[i]
            isLayoutTreeEqual = child.isLayoutTreeEqualToNode(otherNodeChildren)
            if (!isLayoutTreeEqual) break
            i++
        }
        return isLayoutTreeEqual
    }

    fun reset() {
        GlobalMembers.YGAssertWithNode(
            this,
            children_.size == 0,
            "Cannot reset a node which still has children attached"
        )
        GlobalMembers.YGAssertWithNode(
            this,
            owner_ == null,
            "Cannot reset a node still attached to a owner"
        )
        clearChildren()
        val webDefaults: Boolean =
            com.joingo.yoga.internal.detail.GlobalMembers.Companion.getBooleanData(
                flags,
                useWebDefaults_
            )
        config_ = YGConfig(null)
        if (webDefaults) {
            useWebDefaults()
        }
    }

    fun getContext(): Any? {
        return context_
    }

    fun setContext(context_: Any?) {
        this.context_ = context_
    }

    fun getFlags(): Map<Any?, Any> {
        return flags
    }

    fun setFlags(flags: MutableMap<Any?, Any>) {
        this.flags = flags
    }

    fun getReserved(): Byte {
        return reserved_
    }

    fun setReserved(reserved_: Byte) {
        this.reserved_ = reserved_
    }

    fun getMeasure(): measure_Struct {
        return measure_
    }

    fun setMeasure(measure_: measure_Struct) {
        this.measure_ = measure_
    }

    fun getBaseline(): baseline_Struct {
        return baseline_
    }

    fun setBaseline(baseline_: baseline_Struct) {
        this.baseline_ = baseline_
    }

    fun getPrint(): print_Struct {
        return print_
    }

    fun setPrint(print_: print_Struct) {
        this.print_ = print_
    }

    fun getDirtied(): YGDirtiedFunc? {
        return dirtied_
    }

    fun setDirtied(dirtied_: YGDirtiedFunc?) {
        this.dirtied_ = dirtied_
    }

    fun getStyle(): YGStyle {
        return style_
    }

    fun setStyle(style_: YGStyle) {
        this.style_ = style_
    }

    fun getLayout(): YGLayout? {
        return layout_
    }

    fun setLayout(layout_: YGLayout?) {
        var layout_ = layout_
        if (layout_ == null) layout_ = YGLayout()
        this.layout_ = layout_
    }

    fun getLineIndex(): Int {
        return lineIndex_
    }

    fun setLineIndex(lineIndex_: Int) {
        this.lineIndex_ = lineIndex_
    }

    fun getOwner(): YGNode? {
        return owner_
    }

    fun setOwner(owner_: YGNode?) {
        this.owner_ = owner_
    }

    fun getChildren(): ArrayList<YGNode> {
        return children_
    }

    fun setChildren(children_: ArrayList<YGNode>) {
        this.children_ = children_
    }

    fun getConfig(): YGConfig? {
        return config_
    }

    fun setConfig(config_: YGConfig?) {
        this.config_ = config_
    }

    fun getResolvedDimensions(): ArrayList<YGValue> {
        return resolvedDimensions_
    }

    fun setResolvedDimensions(resolvedDimensions_: ArrayList<YGValue?>) {
        this.resolvedDimensions_ = resolvedDimensions_
    }

    class measure_Struct {
        var noContext: YGMeasureFunc? = null
        var withContext: MeasureWithContextFn? = null
    }

    class baseline_Struct {
        var noContext: YGBaselineFunc? = null
        var withContext: BaselineWithContextFn? = null
    }

    class print_Struct {
        var noContext: YGPrintFunc? = null
        var withContext: PrintWithContextFn? = null
    }

    companion object {
        private const val hasNewLayout_ = 0
        private const val isReferenceBaseline_ = 1
        private const val isDirty_ = 2
        private const val nodeType_ = 3
        private const val measureUsesContext_ = 4
        private const val baselineUsesContext_ = 5
        private const val printUsesContext_ = 6
        private const val useWebDefaults_ = 7
        fun computeEdgeValueForRow(
            edges: Values<YGEdge>,
            rowEdge: YGEdge,
            edge: YGEdge,
            defaultValue: CompactValue
        ): CompactValue //Method definition originates from: YGNode.cpp
        {
            return if (!edges.getCompactValue(rowEdge).isUndefined()) {
                edges.getCompactValue(rowEdge)
            } else if (!edges.getCompactValue(edge).isUndefined()) {
                edges.getCompactValue(edge)
            } else if (!edges.getCompactValue(YGEdge.YGEdgeHorizontal).isUndefined()) {
                edges.getCompactValue(YGEdge.YGEdgeHorizontal)
            } else if (!edges.getCompactValue(YGEdge.YGEdgeAll).isUndefined()) {
                edges.getCompactValue(YGEdge.YGEdgeAll)
            } else {
                defaultValue
            }
        }

        fun computeEdgeValueForColumn(
            edges: Values<YGEdge>,
            edge: YGEdge,
            defaultValue: CompactValue
        ): CompactValue //Method definition originates from: YGNode.cpp
        {
            return if (!edges.getCompactValue(edge).isUndefined()) {
                edges.getCompactValue(edge)
            } else if (!edges.getCompactValue(YGEdge.YGEdgeVertical).isUndefined()) {
                edges.getCompactValue(YGEdge.YGEdgeVertical)
            } else if (!edges.getCompactValue(YGEdge.YGEdgeAll).isUndefined()) {
                edges.getCompactValue(YGEdge.YGEdgeAll)
            } else {
                defaultValue
            }
        }
    }
}