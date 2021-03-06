package com.cyl.musiclake.ui.chat

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musicapi.playlist.MusicInfo
import com.cyl.musicapi.playlist.UserInfo
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.bean.MessageEvent
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.socket.SocketManager
import com.cyl.musiclake.ui.music.dialog.BottomDialogFragment
import com.cyl.musiclake.utils.ConvertUtils
import com.cyl.musiclake.utils.CoverLoader

/**
 * 作者：yonglong on 2016/8/10 21:36
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class ChatListAdapter(list: List<MessageEvent>) : BaseQuickAdapter<MessageEvent, BaseViewHolder>(R.layout.item_chat, list) {

    override fun convert(holder: BaseViewHolder, item: MessageEvent) {
        holder.setText(R.id.tv_comment_user, item.userInfo?.nickname ?: "")
        holder.setText(R.id.tv_comment_time, item.datetime)
        holder.setText(R.id.tv_comment_content, item.message)
        holder.getView<TextView>(R.id.tv_comment_content).setTextIsSelectable(true)
        CoverLoader.loadImageView(mContext, item.userInfo?.avatar, holder.getView(R.id.civ_cover))
        if (item.type == SocketManager.MESSAGE_BROADCAST) {
            holder.getView<View>(R.id.include_music).visibility = View.GONE
        } else if (item.type == SocketManager.MESSAGE_SHARE) {
            try {
                item.music = MusicApp.GSON.fromJson(item.message, MusicInfo::class.java)
            } catch (e: Throwable) {
            } finally {
                if (item.music != null) {
                    holder.getView<View>(R.id.include_music).visibility = View.VISIBLE
                    holder.getView<View>(R.id.tv_comment_content).visibility = View.GONE
                    val music = MusicUtils.getMusic(item.music!!)
                    holder.setText(R.id.tv_title, ConvertUtils.getTitle(music.title))
                    holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(music.artist, music.album))
                    if (music.type == Constants.LOCAL) {
                        holder.getView<View>(R.id.iv_resource).visibility = View.GONE
                    } else {
                        holder.getView<View>(R.id.iv_resource).visibility = View.VISIBLE
                        when (music.type) {
                            Constants.BAIDU -> {
                                holder.setImageResource(R.id.iv_resource, R.drawable.baidu)
                            }
                            Constants.NETEASE -> {
                                holder.setImageResource(R.id.iv_resource, R.drawable.netease)
                            }
                            Constants.QQ -> {
                                holder.setImageResource(R.id.iv_resource, R.drawable.qq)
                            }
                            Constants.XIAMI -> {
                                holder.setImageResource(R.id.iv_resource, R.drawable.xiami)
                            }
                        }
                    }
                    if (music.coverUri != null) {
                        CoverLoader.loadImageView(mContext, music.coverUri, holder.getView(R.id.iv_cover))
                    }
                    holder.getView<View>(R.id.include_music).setOnClickListener {
                        PlayManager.playOnline(music)
                    }
                    holder.getView<View>(R.id.iv_more).setOnClickListener {
                        BottomDialogFragment.newInstance(music).show(mContext as ChatActivity)
                    }
                }
            }
        }
    }

}

//class ChatMultiListAdapter(val context: Context, val list: MutableList<MessageEvent>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    var data = mutableListOf<MessageEvent>()
//
//    init {
//        data = list
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = data[position]
//        if (holder is MusicViewHolder) {
//            holder.messageTimeTv.text = item.datetime
//            holder.userNameTv.text = item.userInfo?.nickname
//            CoverLoader.loadImageView(context, item.userInfo?.avatar, holder.coverIv)
////            val music = MusicApp.GSON.fromJson<Music>(item.message, Music::class.java)
////
////            holder.titleTv.text = music.title
////            holder.artistTv.text = music.artist
////            CoverLoader.loadImageView(context, music.coverUri, holder.songCoverIv)
////            holder.playingView.visibility = if (PlayManager.getPlayingId() == music.mid) View.VISIBLE else View.GONE
////            if (music.type != Constants.LOCAL) {
////                holder.resourceIv.visibility = View.VISIBLE
////                holder.resourceIv.setImageResource(
////                        when (music.type) {
////                            Constants.BAIDU -> {
////                                R.drawable.baidu
////                            }
////                            Constants.QQ -> {
////                                R.drawable.qq
////                            }
////                            Constants.XIAMI -> {
////                                R.drawable.xiami
////                            }
////                            Constants.NETEASE -> {
////                                R.drawable.netease
////                            }
////                            else -> {
////                                R.drawable.netease
////                            }
////                        }
////                )
////            }
////            holder.itemView.setOnClickListener {
////                PlayManager.playOnline(music)
////            }
////            holder.moreIv.setOnClickListener {
////                BottomDialogFragment.newInstance(music).show(context as ChatActivity)
////            }
//        } else if (holder is ViewHolder) {
//            holder.messageTv.text = item.message
//            holder.messageTimeTv.text = item.datetime
//            holder.userNameTv.text = item.userInfo?.nickname
//            CoverLoader.loadImageView(context, item.userInfo?.avatar, holder.coverIv)
//        }
//    }
//
//    fun setNewData(list: MutableList<MessageEvent>) {
//        data = list
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == 1) {
//            val view = LayoutInflater.from(context).inflate(R.layout.item_chat_music, parent, false)
//            MusicViewHolder(view)
//        } else {
//            val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
//            ViewHolder(view)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return data[position].type
//    }
//
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val userNameTv = itemView.findViewById<TextView>(R.id.tv_comment_user)
//        val messageTimeTv = itemView.findViewById<TextView>(R.id.tv_comment_time)
//        val messageTv = itemView.findViewById<TextView>(R.id.tv_comment_content)
//        val coverIv = itemView.findViewById<ImageView>(R.id.civ_cover)
//    }
//
//    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val userNameTv = itemView.findViewById<TextView>(R.id.tv_comment_user)
//        val messageTimeTv = itemView.findViewById<TextView>(R.id.tv_comment_time)
//        val messageTv = itemView.findViewById<TextView>(R.id.tv_comment_content)
//        val coverIv = itemView.findViewById<ImageView>(R.id.civ_cover)
//
//        val titleTv = itemView.findViewById<TextView>(R.id.tv_title)
//        val artistTv = itemView.findViewById<TextView>(R.id.tv_artist)
//        val songCoverIv = itemView.findViewById<ImageView>(R.id.iv_cover)
//        val playingView = itemView.findViewById<View>(R.id.v_playing)
//        val resourceIv = itemView.findViewById<ImageView>(R.id.iv_resource)
//        val moreIv = itemView.findViewById<ImageView>(R.id.iv_more)
//    }
//}


class OnlineUserListAdapter(list: List<UserInfo>) : BaseQuickAdapter<UserInfo, BaseViewHolder>(R.layout.item_user, list) {
    private var isSetOnClick = false
    override fun convert(helper: BaseViewHolder, item: UserInfo) {
        CoverLoader.loadImageView(mContext, item.avatar, helper.getView(R.id.user_avatar))
        helper.setText(R.id.user_name, item.nickname)
        helper.itemView.setOnClickListener {
            if (isSetOnClick) return@setOnClickListener
            isSetOnClick = true
            helper.getView<TextView>(R.id.user_name).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.user_name).scaleX = 0f
            helper.getView<TextView>(R.id.user_name).scaleY = 0f
            helper.getView<TextView>(R.id.user_name).animate().scaleY(1f).scaleX(1f).setDuration(300).start()
            helper.getView<TextView>(R.id.user_name).postDelayed({
                helper.getView<TextView>(R.id.user_name).animate().scaleY(0f).scaleX(0f).setDuration(300).start()
                isSetOnClick = false
                helper.getView<TextView>(R.id.user_name).postDelayed({ helper.getView<TextView>(R.id.user_name).visibility = View.GONE }, 300)
            }, 3000)
        }
    }

}
