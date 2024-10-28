#include <jni.h>

#include <string>
#include <list>
#include <vector>

using namespace std;

// Assuming you have a C++ class or struct to represent Question
struct Question {
    string line;
    bool answer;
};
jobjectArray convertToJavaArrayList(JNIEnv *env, const vector<Question>& questions) {
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID initMethod = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID addMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jobject arrayList = env->NewObject(arrayListClass, initMethod);

    jclass questionClass = env->FindClass("com/example/movies/model/Question");
    jmethodID questionConstructor = env->GetMethodID(questionClass, "<init>", "(Ljava/lang/String;Z)V");

    for (const auto& question : questions) {
        jstring line = env->NewStringUTF(question.line.c_str());
        jboolean answer = question.answer;
        jobject javaQuestion = env->NewObject(questionClass, questionConstructor, line, answer);

        env->CallBooleanMethod(arrayList, addMethod, javaQuestion);

        env->DeleteLocalRef(line);
        env->DeleteLocalRef(javaQuestion);
    }

    return (jobjectArray)arrayList;
}

extern "C"

JNIEXPORT jobject JNICALL
Java_com_example_movies_activity_GameActivity_setQuestions(JNIEnv *env, jobject thiz) {
    vector<Question> questions={
            {"Мельбурн — столица Австралии",false},
            {"Череп – самая крепкая кость в человеческом теле", false},
            {"Google изначально назывался BackRub", true},
            {"Помидоры - это фрукты",true},
            {"Клеопатра была египетского происхождения", false},
            {"Бананы - это ягоды",true},
            {"Coca-Cola существует во всех странах мира",false},
            {"Курица может жить без головы еще долго после того, как ее отрубили",true},
            {"ДНК людей на 95 процентов совпадает с бананами",false},
            {"Жирафы говорят «му»",true},
            {"Все млекопитающие живут на суше",false},
            {"Кофе готовят из ягод", true},
            {"Животное с самым большим мозгом по отношению к телy - мyравей", true},
            {"Около 70 процентов живых сyществ Земли - бактерии", true},
            {"У улитки около 25 000 зубов", true},
            {"Крокодилы глотают камни, чтобы глубже нырнуть",true}
    };

    return convertToJavaArrayList(env, questions);
}

