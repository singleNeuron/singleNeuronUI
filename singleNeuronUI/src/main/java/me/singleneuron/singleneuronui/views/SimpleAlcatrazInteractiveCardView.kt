package me.singleneuron.singleneuronui.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.getBooleanOrThrow
import androidx.core.content.res.getColorOrThrow
import me.singleneuron.singleneuronui.R
import me.singleneuron.singleneuronui.databinding.SimpleAlcatrazInteractiveCardviewBinding

open class SimpleAlcatrazInteractiveCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.style.googleBlue) : LinearLayout(context, attrs, defStyleAttr) {

    protected val binding: SimpleAlcatrazInteractiveCardviewBinding = SimpleAlcatrazInteractiveCardviewBinding.inflate(LayoutInflater.from(context), this, true)

    var active: Boolean = false
        set(value) {
            val color = if (value) ContextCompat.getColor(context, R.color.green)
                else ContextCompat.getColor(context, R.color.colorError)
            this.color = color
            val drawable = if (value) R.drawable.ic_check_circle else R.drawable.ic_cancel
            this.iconDrawable = context.getDrawable(drawable)
            this.iconTint = Color.WHITE
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayIndicatorImage.visibility = View.VISIBLE
            field = value
        }

    @ColorInt
    var color: Int = ContextCompat.getColor(context, R.color.colorPrimary)
        set(value) {
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayLayer.setBackgroundColor(value)
            field = value
        }

    @DrawableRes
    var iconRes: Int = -1
        set(value) {
            if (value==-1) return
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayIndicatorImage.setImageResource(value)
            field = value
        }

    var iconDrawable: Drawable? = null
        set(value) {
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayIndicatorImage.setImageDrawable(value)
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayIndicatorImage.visibility = if (value==null) View.VISIBLE else View.GONE
            field = value
        }

    @ColorInt
    var iconTint: Int = Color.WHITE
        set(value) {
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayIndicatorImage.setColorFilter(value)
            field = value
        }

    var firstLine: String?
        get() {
            return binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayInfo.text.toString()
        }
        set(value) {
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayInfo.text = value
        }

    var secondLine: String?
        get() {
            return binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlaySubinfo.text.toString()
        }
        set(value) {
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlaySubinfo.text = value
        }

    var titleLine: String?
        get() {
            return binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlaySubinfo.toString()
        }
        set(value) {
            binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayTitle.text = value
        }

    init {
        binding.alcatrazInteractiveCard.setShowOverlay(true)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SimpleAlcatrazInteractiveCardView, 0, 0)
        try {
            firstLine = a.getString(R.styleable.SimpleAlcatrazInteractiveCardView_secondLine)
            secondLine = a.getString(R.styleable.SimpleAlcatrazInteractiveCardView_thirdLine)
            titleLine = a.getString(R.styleable.SimpleAlcatrazInteractiveCardView_titleLine)
            color = a.getColor(R.styleable.SimpleAlcatrazInteractiveCardView_backgroundColor, ContextCompat.getColor(context, R.color.colorPrimary))
            iconDrawable = a.getDrawable(R.styleable.SimpleAlcatrazInteractiveCardView_iconDrawable)
            try {
                iconTint = a.getColorOrThrow(R.styleable.SimpleAlcatrazInteractiveCardView_iconDrawableTint)
            } catch (e:Exception) {
                //ignored
            }
            try {
                active = a.getBooleanOrThrow(R.styleable.SimpleAlcatrazInteractiveCardView_active)
            } catch (e:Exception) {
                //ignored
            }
        } finally {
            a.recycle()
        }
        binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayIndicatorImage.visibility = View.VISIBLE
        binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayTitle.visibility = View.VISIBLE
        binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlayInfo.visibility = View.VISIBLE
        binding.alcatrazInteractiveCard.cardBinding.alcInteractiveCardOverlaySubinfo.visibility = View.VISIBLE
    }

}