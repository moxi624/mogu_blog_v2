<template>
  <span class="emoji-item-common" :class="['emoji-'+text,isInput?'emoji-size-small':'emoji-size-large']" @click="emojiClick"></span>
</template>
<script>
export default {
  data() {
    return {};
  },
  components: {},
  props: ["text", "isInput"],
  methods: {
    emojiClick() {
      this.$emit("onClick", this.text);
    }
  }
};
</script>
