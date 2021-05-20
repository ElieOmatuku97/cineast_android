package elieomatuku.cineast_android.ui.content_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import elieomatuku.cineast_android.core.model.Content
import elieomatuku.cineast_android.ui.activity.ToolbarMVPActivity
import elieomatuku.cineast_android.utils.UiUtils
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback
import java.util.ArrayList

class ContentListActivity : ToolbarMVPActivity<ContentListPresenter, ContentListVu>() {
    companion object {
        private val MVP_UID by lazy {
            hashCode()
        }

        fun startItemListActivity(context: Context, contents: List<Content>, screenNameRes: Int? = null) {
            val intent = Intent(context, ContentListActivity::class.java)
            val params = Bundle()
            params.putParcelableArrayList(UiUtils.WIDGET_KEY, contents as ArrayList<out Parcelable>)

            if (screenNameRes != null) {
                params.putInt(UiUtils.SCREEN_NAME_KEY, screenNameRes)
            }

            intent.putExtras(params)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, true)
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<ContentListPresenter, ContentListVu> {
        return MVPDispatcher(
                MVP_UID,
                PresenterCacheLoaderCallback(this) { ContentListPresenter() },
                ::ContentListVu
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }
}
