package elieomatuku.cineast_android.ui.discover

//import android.app.Activity
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.res.ResourcesCompat
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import elieomatuku.cineast_android.R
//import elieomatuku.cineast_android.domain.model.*
//import elieomatuku.cineast_android.ui.base.BaseVu
//import elieomatuku.cineast_android.ui.fragment.WebviewFragment
//import elieomatuku.cineast_android.ui.home.HomeActivity
//import elieomatuku.cineast_android.ui.settings.LoginWebviewFragment
//import elieomatuku.cineast_android.ui.utils.WebLink
//import io.chthonic.mythos.mvp.FragmentWrapper
//import io.reactivex.Observable
//import io.reactivex.subjects.PublishSubject
//import kotlinx.android.synthetic.main.fragment_discover.view.*
//import timber.log.Timber
//
//class DiscoverVu(
//    inflater: LayoutInflater,
//    activity: Activity,
//    fragmentWrapper: FragmentWrapper?,
//    parentView: ViewGroup?
//) : BaseVu(
//    inflater,
//    activity = activity,
//    fragmentWrapper = fragmentWrapper,
//    parentView = parentView
//),
//    WebLink<AccessToken?> {
//
//    private val movieSelectPublisher: PublishSubject<Movie> by lazy {
//        PublishSubject.create<Movie>()
//    }
//    val movieSelectObservable: Observable<Movie>
//        get() = movieSelectPublisher.hide()
//
//    private val personSelectPublisher: PublishSubject<Person> by lazy {
//        PublishSubject.create<Person>()
//    }
//
//    val personSelectObservable: Observable<Person>
//        get() = personSelectPublisher.hide()
//
//    private val loginClickPublisher: PublishSubject<Boolean> by lazy {
//        PublishSubject.create<Boolean>()
//    }
//
//    val loginClickObservable: Observable<Boolean>
//        get() = loginClickPublisher.hide()
//
//    private val listView: RecyclerView by lazy {
//        rootView.recyclerview
//    }
//
//    private val refreshLayout: SwipeRefreshLayout by lazy {
//        rootView.refreshLayout
//    }
//
//    private val refreshPublisher: PublishSubject<Boolean> by lazy {
//        PublishSubject.create<Boolean>()
//    }
//
//    val refreshObservable: Observable<Boolean>
//        get() = refreshPublisher.hide()
//
//    private val sessionPublisher: PublishSubject<Pair<String, Account>> by lazy {
//        (activity as HomeActivity).sessionPublisher
//    }
//
//    val sessionObservable: Observable<Pair<String, Account>> by lazy {
//        sessionPublisher.hide()
//    }
//
//    val adapter: DiscoverAdapter by lazy {
//        DiscoverAdapter(movieSelectPublisher, personSelectPublisher, loginClickPublisher)
//    }
//
//    override fun getRootViewLayoutId(): Int {
//        return R.layout.fragment_discover
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        listView.adapter = adapter
//        val itemDecoration = DividerItemDecoration(listView.context, DividerItemDecoration.VERTICAL)
//        val drawable: Drawable? = ResourcesCompat.getDrawable(
//            activity.resources,
//            R.drawable.item_decoration,
//            activity.theme
//        )
//        if (drawable != null) {
//            itemDecoration.setDrawable(drawable)
//        }
//        listView.addItemDecoration(itemDecoration)
//        listView.layoutManager = LinearLayoutManager(activity)
//
//        refreshLayout.setOnRefreshListener {
//            refreshPublisher.onNext(true)
//        }
//    }
//
//    fun updateView(
//        discoverContent: elieomatuku.cineast_android.domain.DiscoverContent,
//        isLoggedIn: Boolean
//    ) {
//        Timber.d("update View is called")
//        adapter.filteredContents = discoverContent.getFilteredWidgets()
//        adapter.isLoggedIn = isLoggedIn
//        adapter.notifyDataSetChanged()
//        listView.visibility = View.VISIBLE
//        dismissRefreshLayout()
//    }
//
//    fun updateErrorView(errorMsg: String?) {
//        adapter.errorMessage = errorMsg
//        adapter.notifyDataSetChanged()
//        listView.visibility = View.VISIBLE
//        dismissRefreshLayout()
//    }
//
//    override fun gotoWebview(value: AccessToken?) {
//        value?.let {
//            val authenticateUrl = Uri.parse(activity.getString(R.string.authenticate_url))
//                .buildUpon()
//                .appendPath(it.requestToken)
//                .build()
//                .toString()
//
//            val webviewFragment: WebviewFragment? =
//                LoginWebviewFragment.newInstance(authenticateUrl)
//            val fm = (activity as AppCompatActivity).supportFragmentManager
//
//            if (webviewFragment != null && fm != null) {
//                fm.beginTransaction().add(android.R.id.content, webviewFragment, null)
//                    .addToBackStack(null).commit()
//            }
//        }
//    }
//
//    fun updateLoginState(isLoggedIn: Boolean) {
//        adapter.isLoggedIn = isLoggedIn
//        adapter.notifyDataSetChanged()
//    }
//
//    private fun dismissRefreshLayout() {
//        refreshLayout.setRefreshing(false)
//    }
//}
