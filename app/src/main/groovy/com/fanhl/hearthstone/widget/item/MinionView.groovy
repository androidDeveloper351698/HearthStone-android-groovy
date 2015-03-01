package com.fanhl.hearthstone.widget.item

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.fanhl.hearthstone.R
import com.fanhl.hearthstone.factory.CardBuilder
import com.fanhl.hearthstone.model.card.Minion

/**
 * 卡牌视图(只显示随从上场的视图,其它形态使用其它的View来表示)
 *
 * 由于长宽比是固定的,所以只需要设定宽度(具体值),设定高度没有用
 *
 * 不接收padding参数
 *
 * Created by fanhl on 15/2/25.
 */
public class MinionView extends AbstractView {
    /**高:宽*/
    public static final float HEIGHT2WIDTH_RATE = 1.5f

    public static final float TITLE2WIDTH_RATE = 0.15f
    public static final float DESCRIPTION2WIDTH_RATE = 0.1f
    /**种族字体大小:宽*/
    public static final float RACE2WIDTH_RATE = 0.08f
    /**费|攻击|血|耐久:宽*/
    public static final float MINT2WIDTH_RATE = 0.2f

    Minion minion

    /**图案*/
    Drawable pattern
    Drawable cardBackground

    //以下用来绘制文字
    TextDrawerHolder attackHolder
    TextDrawerHolder bloodHolder
    TextDrawerHolder titleHolder

    private Paint errPaint

    public MinionView(Context context) {
        super(context)
        init(null, 0)
    }

    public MinionView(Context context, AttributeSet attrs) {
        super(context, attrs)
        init(attrs, 0)
    }

    public MinionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle)
        init(attrs, defStyle)
    }

    def bind(Minion minion) {
        this.minion = minion
//        pattern=getResources().getDrawable(card.patternId)//FIXME
        cardBackground = getResources().getDrawable(R.drawable.weapon_view_background)//FIXME

        initPaint()

        invalidate()
    }

    private void init(AttributeSet attrs, int defStyle) {
        errPaint = new Paint()
        errPaint.setColor(Color.RED)

        //FIXME 测试用
        CardBuilder.init()
        bind(CardBuilder.newCard(100001))
    }

    private void initPaint() {
        titleHolder = new TextDrawerHolder()


        attackHolder = new TextDrawerHolder()
        bloodHolder = new TextDrawerHolder()
    }

    private void invalidatePaintAndMeasurements() {

        //标题
        titleHolder.setParams(minion.title, width * TITLE2WIDTH_RATE as float,
                width / 2 as float, height * 0.7f as float, Datum.CENTER)

        attackHolder.setParams(minion.attack.current.toString(), width * MINT2WIDTH_RATE as float,
                width * 0.1 as float, height * 0.8f as float, Datum.CENTER)
        bloodHolder.setParams(minion.blood.current.toString(), width * MINT2WIDTH_RATE as float,
                width * 0.9 as float, height * 0.8f as float, Datum.CENTER)

        //FIXME 之后这些x,y坐标全部要换成constants

        //FIXME 测试用
//        throw new Exception("""width:$width height:$height
//cost[x:${costHolder.x} y:${costHolder.y}
//attack[x:${attackHolder.x} y:${attackHolder.y}
//blood[x:${bloodHolder.x} y:${bloodHolder.y}
//""")
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec)
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec)

        if (widthMode != android.view.View.MeasureSpec.EXACTLY) {
            throw new Exception("not MeasureSpec.EXACTLY mode!")
        }

        int heightSize = widthSize * HEIGHT2WIDTH_RATE
        int newHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(heightSize, widthMode)

        Object.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Object.onDraw(canvas)

        if (!minion) {
            canvas.drawRect(0, 0, width, height, errPaint)
            return
        }

        invalidatePaintAndMeasurements()

        cardBackground?.with {
            setBounds(0, 0, width, height)
            draw(canvas)
        }

        titleHolder.draw(canvas)

        attackHolder.draw(canvas)
        bloodHolder.draw(canvas)


    }

}
