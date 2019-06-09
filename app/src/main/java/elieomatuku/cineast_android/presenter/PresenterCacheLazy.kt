package elieomatuku.restapipractice.presenter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import io.chthonic.mythos.mvp.Presenter
import io.chthonic.mythos.mvp.PresenterCache



class PresenterCacheLazy<P>(oneTimePresenterCreator: ()->P) : PresenterCache<P>() where P : Presenter<*>{
    private var presenterCreator: (()->P)? = oneTimePresenterCreator

    override var presenter: P? = null
        get() {
            if (field == null) {
                presenterCreator?.let{
                    field = it()
                }
                presenterCreator = null
            }
            return field
        }

}