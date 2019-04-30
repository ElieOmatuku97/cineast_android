package elieomatuku.restapipractice.activity

import android.os.Bundle
import elieomatuku.restapipractice.presenter.ItemListPresenter
import elieomatuku.restapipractice.utils.UiUtils
import elieomatuku.restapipractice.vu.ItemListVu
import io.chthonic.mythos.mvp.MVPDispatcher
import io.chthonic.mythos.mvp.PresenterCacheLoaderCallback

class ItemListActivity: ToolbarMVPActivity <ItemListPresenter, ItemListVu>() {
    companion object {
        private val MVP_UID by lazy {
            ItemListActivity.hashCode()
        }
    }

    override fun createMVPDispatcher(): MVPDispatcher<ItemListPresenter, ItemListVu> {
        return MVPDispatcher(MVP_UID,
                PresenterCacheLoaderCallback(this, { ItemListPresenter() }),
                ::ItemListVu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mvpDispatcher.vu?.toolbar?.let {
            UiUtils.initToolbar(this, it, true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            onBackPressed()
        }
        return true
    }
}