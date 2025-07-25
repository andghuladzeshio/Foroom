package com.example.foroom.presentation.ui.screens.home.chats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alternator.foroom.databinding.ItemForoomChatBinding
import com.example.foroom.presentation.ui.model.ChatUI
import com.example.foroom.presentation.ui.util.adapter.ForoomLoadingListAdapter
import com.example.shared.extension.isEven

class ForoomChatsAdapter(
    onLoadMore: () -> Unit
) : ForoomLoadingListAdapter<ChatUI>(onLoadMore) {
    var onSendButtonClicked: (ChatUI) -> Unit = {}
    var onFavoriteButtonClicked: (ChatUI) -> Unit = {}
    var onRemoveButtonClicked: (ChatUI)-> Unit = {}

    override fun onCreateDataItemViewHolder(
        parent: ViewGroup, viewType: Int
    ): LoadingListDataViewHolder<ChatUI> {
        return ViewHolder(
            ItemForoomChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @Suppress("UNCHECKED_CAST")
    inner class ViewHolder(
        private val binding: ItemForoomChatBinding
    ) : LoadingListDataViewHolder<ChatUI>(binding) {

        init {
            setUp()
        }

        private fun setUp() {
            with(binding.root) {
                onSendMessageButtonClick = {
                    onSendButtonClicked(getChatItem())
                }

                onStarButtonClick = {
                    onFavoriteButtonClicked(getChatItem())
                }

                onRemoveButtonClick = {
                    onRemoveButtonClicked(getChatItem())
                }
            }
        }

        private fun getChatItem() =
            (currentList[adapterPosition] as LoadingListItemType.DataItem<ChatUI>).data

        override fun onBind(item: ChatUI, position: Int) {
            val angle = if (position.isEven()) POSITIVE_ANGLE else NEGATIVE_ANGLE

            with(binding.root) {
                setChatTitle(item.name)
                setAuthorName(item.creatorUsername)
                setImageUrl(item.emojiUrl)
                setIsFavorite(item.isFavorite)

                if (item.createdByCurrentUser) showRemoveButton() else hideRemoveButton()

                rotation = angle
            }
        }
    }

    companion object {
        private const val POSITIVE_ANGLE = 6F
        private const val NEGATIVE_ANGLE = -6F
    }
}