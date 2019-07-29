<template>
  <section class="todoapp">
    <!-- header -->
    <header class="header">
      <input class="new-todo" autocomplete="off" placeholder="待办事项" @keyup.enter="addTodo">
    </header>
    <!-- main section -->
    <section class="main" v-show="todos.length">
      <input
        class="toggle-all"
        id="toggle-all"
        type="checkbox"
        :checked="allChecked"
        @change="toggleAll({ done: !allChecked })"
      >
      <label for="toggle-all"></label>
      <ul class="todo-list">
        <todo
          @toggleTodo="toggleTodo"
          @editTodo="editTodo"
          @deleteTodo="deleteTodo"
          v-for="(todo, index) in filteredTodos"
          :key="index"
          :todo="todo"
        ></todo>
      </ul>
    </section>
    <!-- footer -->
    <footer class="footer" v-show="todos.length">
      <span class="todo-count">
        <strong>{{ remaining }}</strong>
        {{ remaining | pluralize('item') }} 
      </span>
      <ul class="filters">
        <li v-for="(val, key) in filters" :key="key">
          <a
            :class="{ selected: visibility === key }"
            @click.prevent="visibility = key"
          >{{ key | capitalize }}</a>
        </li>
      </ul>
      <!-- <button class="clear-completed" v-show="todos.length > remaining" @click="clearCompleted">
        Clear completed
      </button>-->
    </footer>
  </section>
</template>

<script>
import Todo from "./Todo.vue";
import { getList, addTodo, editTodo, deleteTodo, toggleAll } from "@/api/todo";

const STORAGE_KEY = "todos";

const filters = {
  active: todos => todos.filter(todo => !todo.done),
  completed: todos => todos.filter(todo => todo.done),
  all: todos => todos
};

export default {

  components: { Todo },
  data() {
    return {
      visibility: "active",
      filters,
      // todos: JSON.parse(window.localStorage.getItem(STORAGE_KEY)) || defalutList
      todos: []
    };
  },
  created() {
    getList().then(response => {
      console.log("待办事项", response);
      if (response.code == "success") {
        this.todos = response.data.records;
      }
    });
  },
  computed: {
    allChecked() {
      return this.todos.every(todo => todo.done);
    },
    filteredTodos() {
      return filters[this.visibility](this.todos);
    },
    remaining() {
      return this.todos.filter(todo => !todo.done).length;
    }
  },
  methods: {
    setLocalStorgae() {
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify(this.todos));
    },

    getTodoList() {
      getList().then(response => {
        console.log("待办事项", response);
        if (response.code == "success") {
          this.todos = response.data.records;
        }
      });
    },

    // 添加代办事项
    addTodo(e) {
      const text = e.target.value;
      if (text.trim()) {
        var params = new URLSearchParams();
        params.append("text", text.trim());

        addTodo(params).then(response => {
          console.log("添加事项", response);
          if (response.code == "success") {
            this.getTodoList();
          }
        });
      }
      e.target.value = "";
    },

    toggleTodo(val) {
      console.log("开始编辑", val);      
      var params = new URLSearchParams();      
      params.append("done", !val.done);      
      params.append("uid", val.uid);
      editTodo(params).then(response => {
        if (response.code == "success") {
          this.getTodoList();
        }
      });

    },
    deleteTodo(todo) {
      var params = new URLSearchParams();
      params.append("uid", todo.uid);
      deleteTodo(params).then(response => {
        if (response.code == "success") {
          this.getTodoList();
        }
      });
    },
    editTodo({ todo, value }) {
      var params = new URLSearchParams();
      params.append("text", value.trim());
      params.append("uid", todo.uid);
      editTodo(params).then(response => {
        if (response.code == "success") {
          this.getTodoList();
        }
      });
    },

    // 删除已经完成的事项
    clearCompleted() {
      this.todos = this.todos.filter(todo => !todo.done);
      this.setLocalStorgae();
    },

    //切换
    toggleAll({ done }) {
      // this.todos.forEach(todo => {
      //   todo.done = done;
      //   this.setLocalStorgae();
      // });

      var params = new URLSearchParams();
      params.append("done", done);
      toggleAll(params).then(response => {
        if (response.code == "success") {
          this.getTodoList();
        }
      });
    }
  },
  filters: {
    pluralize: (n, w) => (n === 1 ? w : w + "s"),
    capitalize: s => s.charAt(0).toUpperCase() + s.slice(1)
  }
};
</script>

<style lang="scss">
@import "./index.scss";
</style>
